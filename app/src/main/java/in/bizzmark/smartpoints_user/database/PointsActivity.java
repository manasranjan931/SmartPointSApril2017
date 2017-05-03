package in.bizzmark.smartpoints_user.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.store.StoreHomeActivity;
import in.bizzmark.smartpoints_user.adapter.PointsAdapter;
import in.bizzmark.smartpoints_user.bo.EarnPointsBO;
import in.bizzmark.smartpoints_user.sqlitedb.DbHelper;

public class PointsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_Arrow_Back,ivSyncData;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ProgressBar pb;

    ArrayList arrayList;
    //String json_url = "http://wwwbizzmarkin.000webhostapp.com/Bizzmark/storeTransactions.php";
    String json_url = "http://wwwbizzmarkin.000webhostapp.com/Bizzmark/sellerEarnTransactions.php";
    String storeName,points,deviceId;
    String final_json;
    Context context = PointsActivity.this;

    DbHelper helper;
    SQLiteDatabase sqLiteDatabase;

    ConnectivityManager cm;
    NetworkInfo networkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        arrayList = new ArrayList();

        // find all ids
        findViewByAllId();

        // Check InternetConnection
        checkInternetConnection();
    }

    private void checkInternetConnection() {
        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo =  cm.getActiveNetworkInfo();
        if (networkInfo == null){
            retrieveDataFromSQLite();
        }else {
            retrieveDataFromServer();
        }
    }

    // Retrieve Data From Server
    private void retrieveDataFromServer() {
        new MyAsynctask().execute();
    }

    // Retrieve Data From SQLite
    private void retrieveDataFromSQLite() {
        helper = new DbHelper(this);
        sqLiteDatabase = helper.getWritableDatabase();

            String query = "SELECT STORE_NAME, POINTS FROM [CUSTOMER_EARN] WHERE TYPE= 'earn'";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        storeName = cursor.getString(0);
                        points = cursor.getString(1);

                        EarnPointsBO earnPointsBO = new EarnPointsBO();
                        earnPointsBO.setStorename(storeName);
                       // earnPointsBO.setPoints(points);
                        //earnPointsBO.setDeviceId(deviceId);

                        arrayList.add(earnPointsBO);

                    } while (cursor.moveToNext());

                    PointsAdapter ma = new PointsAdapter(arrayList, context);
                    recyclerView.setAdapter(ma);
                }
            } else {
                Toast.makeText(context, "You don't have any points", Toast.LENGTH_SHORT).show();
            }
    }

    private void findViewByAllId() {
        iv_Arrow_Back = (ImageView) findViewById(R.id.your_points_back_arrow);
        ivSyncData = (ImageView) findViewById(R.id.iv_sync_data);
        pb = (ProgressBar) findViewById(R.id.pb_points);

        recyclerView  = (RecyclerView) findViewById(R.id.recylerview_your_points);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        iv_Arrow_Back.setOnClickListener(this);
        ivSyncData.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.your_points_back_arrow){
            finish();
        }else if (id == R.id.iv_sync_data){
           /* if (networkInfo != null){
                new MyAsynctask().execute();
            }else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                retrieveDataFromSQLite();
            }*/
           startActivity(new Intent(this, StoreHomeActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkInfo != null){
            new MyAsynctask().execute();
        }else {
            retrieveDataFromSQLite();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkInfo == null){
           retrieveDataFromSQLite();
        }else {
            new MyAsynctask().execute();
        }
    }

    class  MyAsynctask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                final_json   =   getJsonStringFromLink(json_url);
                JSONObject jo = new JSONObject(final_json);
                JSONArray ja = jo.getJSONArray("response");

                for (int i = 0; i < ja.length() ; i++)
                {
                    JSONObject jo1 =  ja.getJSONObject(0);
                    JSONArray ja1 =   jo1.getJSONArray("result");

                    for (int j = 0; j < ja1.length() ; j++)
                    {
                        JSONObject jo2 =  ja1.getJSONObject(j);

                        storeName = jo2.getString("storename");
                        points = jo2.getString("points");
                        deviceId = jo2.getString("deviceid");

                        EarnPointsBO earnPointsBO = new EarnPointsBO();
                        earnPointsBO.setStorename(storeName);
                        earnPointsBO.setPoints(points);

                        arrayList.add(earnPointsBO);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb.setVisibility(View.INVISIBLE);

            PointsAdapter adaptera = new PointsAdapter(arrayList,context);
            recyclerView.setAdapter(adaptera);

        }
    }


    ///////////////   get_json    //////////////////

    public  String getJsonStringFromLink(String json_link) throws IOException
    {
        String JSON_STRING,final_json;

        URL url  = new URL(json_link);
        HttpURLConnection httpCon =   (HttpURLConnection)  url.openConnection();
        InputStream is =  httpCon.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bfr = new BufferedReader(isr);
        StringBuilder s_build = new StringBuilder();
        while ((JSON_STRING = bfr.readLine()) != null)
        {
            s_build.append(JSON_STRING + "\n");
        }

        bfr.close();
        is.close();
        httpCon.disconnect();
        final_json = s_build.toString().trim();

        return  final_json;
    }

///////////////////////////////////////////////////////////

}
