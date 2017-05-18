package in.bizzmark.smartpoints_user.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.adapter.PointsAdapter;
import in.bizzmark.smartpoints_user.bo.EarnPointsBO;
import in.bizzmark.smartpoints_user.sqlitedb.DbHelper;
import in.bizzmark.smartpoints_user.store.StoreHomeActivity;

import static in.bizzmark.smartpoints_user.NavigationActivity.device_Id;

public class PointsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_Arrow_Back,ivSyncData;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ProgressBar pb;

    ArrayList<EarnPointsBO> storeList;
    String storeName,points,storeId;
    Context context = PointsActivity.this;

    DbHelper helper;
    SQLiteDatabase sqLiteDatabase;

    ConnectivityManager cm;
    NetworkInfo networkInfo;

    String json_url = "http://35.154.104.54/smartpoints/customer-api/get-total-points-for-all-stores?customerDeviceId="+device_Id;
    public static String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        storeList = new ArrayList();

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

    // Retrieve Data From Server Using Volley-Library
    private void retrieveDataFromServer() {
      pb.setVisibility(View.VISIBLE);
        storeList.clear();

        StringRequest request = new StringRequest(Request.Method.GET, json_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        pb.setVisibility(View.GONE);

                        try {
                            JSONObject jo = new JSONObject(result);
                            JSONArray ja = jo.getJSONArray("response");

                            for (int j = 0; j < ja.length() ; j++)
                            {
                                JSONObject jo2 =  ja.getJSONObject(j);

                                storeName = jo2.getString("store_name");
                                points = jo2.getString("total_points");
                                storeId = jo2.getString("storeId");

                                EarnPointsBO earnPointsBO = new EarnPointsBO();
                                earnPointsBO.setStorename(storeName);
                                earnPointsBO.setPoints(points);
                                earnPointsBO.setStoreId(storeId);

                                storeList.add(earnPointsBO);
                            }

                            PointsAdapter adapter ;

                            adapter = new PointsAdapter(storeList, context, new PointsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    id = storeList.get(position).getStoreId();
                                    Intent i = new Intent(context,StoreHomeActivity.class);
                                    i.putExtra("storeId",id);
                                    startActivity(i);
                                }
                            });

                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

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
                        earnPointsBO.setPoints(points);
                        //earnPointsBO.setDeviceId(deviceId);

                        storeList.add(earnPointsBO);

                    } while (cursor.moveToNext());
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
           startActivity(new Intent(this, StoreHomeActivity.class));
        }
    }
}
