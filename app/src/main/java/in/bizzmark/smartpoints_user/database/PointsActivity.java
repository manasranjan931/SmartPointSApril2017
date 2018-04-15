package in.bizzmark.smartpoints_user.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import in.bizzmark.smartpoints_user.NavigationActivity;
import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.adapter.PointsAdapter;
import in.bizzmark.smartpoints_user.bo.EarnPointsBO;
import in.bizzmark.smartpoints_user.login.CheckInternet;
import in.bizzmark.smartpoints_user.sqlitedb.DbHelper;
import in.bizzmark.smartpoints_user.store.StoreHomeActivity;

import static in.bizzmark.smartpoints_user.utility.UrlUtility.POINTS_URL;

public class PointsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_Arrow_Back,ivSyncData;
    TextView tvOffline;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ProgressBar pb;
    LinearLayout llErrorMessage, linearLayout;
    Button btnRetry;

    ArrayList<EarnPointsBO> storeList;
    String storeName,points,storeId, type;
    Context context = PointsActivity.this;

    PointsAdapter adapter ;
    Snackbar snackbar;

    DbHelper helper;
    SQLiteDatabase sqLiteDatabase;

    CheckInternet checkInternet = new CheckInternet();

    public static String branchId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        storeList = new ArrayList();

        // find all ids
        findViewByAllId();
    }

    // Retrieve Data From Server Using Volley-Library
    private void retrieveDataFromServer() {
      pb.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        storeList.clear();

            String deviceID= NavigationActivity.device_Id;


            StringRequest request = new StringRequest(Request.Method.GET, POINTS_URL+deviceID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            pb.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            try {
                                JSONObject jo = new JSONObject(result);

                                //Check device transacted or not
                                type = jo.getString("status_type");
                                if (type.equalsIgnoreCase("error")) {
                                    recyclerView.setVisibility(View.GONE);
                                    linearLayout.setVisibility(View.GONE);
                                    llErrorMessage.setVisibility(View.VISIBLE);
                                }

                                JSONArray ja = jo.getJSONArray("response");
                                for (int j = 0; j < ja.length(); j++) {
                                    JSONObject jo2 = ja.getJSONObject(j);

                                    storeName = jo2.getString("store_name");
                                    points = jo2.getString("total_points");
                                    storeId = jo2.getString("storeId");

                                    EarnPointsBO earnPointsBO = new EarnPointsBO();
                                    earnPointsBO.setStorename(storeName);
                                    earnPointsBO.setPoints(points);
                                    earnPointsBO.setStoreId(storeId);

                                    storeList.add(earnPointsBO);
                                }

                                adapter = new PointsAdapter(storeList, context, new PointsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        branchId = storeList.get(position).getStoreId();
                                        Intent i = new Intent(context, StoreHomeActivity.class);
                                        i.putExtra("branchId", branchId);
                                        startActivity(i);
                                    }
                                });

                                recyclerView.setAdapter(adapter);
                                // stopping swipe refresh
                                refreshLayout.setRefreshing(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pb.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "something went wrong please try again", Toast.LENGTH_SHORT).show();
                    // stopping swipe refresh
                    refreshLayout.setRefreshing(false);
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);

    }

    // Retrieve Data From SQLite-Database
    private void retrieveDataFromSQLite() {
        storeList.clear();
        helper = new DbHelper(this);
        sqLiteDatabase = helper.getWritableDatabase();

        if (sqLiteDatabase != null && storeList != null) {
            //String query = "SELECT STORE_NAME, POINTS, BRANCH_ID, STORE_ID FROM CUSTOMER_EARN GROUP BY STORE_NAME";
            //String query = "SELECT STORE_NAME, POINTS, BRANCH_ID, STORE_ID FROM CUSTOMER_EARN_REDEEM WHERE TYPE= 'earn' GROUP BY STORE_NAME";
           // db.rawQuery("select sum(amount) from transaction_table where category = Salary ;", null)




            String query = "SELECT STORE_NAME,Sum(earn_points) - Sum(redeem_points), BRANCH_ID, STORE_ID FROM CUSTOMER_EARN_REDEEM GROUP BY STORE_NAME";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
           // Log.e("ERROR ==>", query);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        storeName = cursor.getString(cursor.getColumnIndex("STORE_NAME"));
                        points = cursor.getString(cursor.getColumnIndex("Sum(earn_points) - Sum(redeem_points)"));
                        branchId = cursor.getString(cursor.getColumnIndex("BRANCH_ID"));
                        storeId = cursor.getString(cursor.getColumnIndex("STORE_ID"));

                        EarnPointsBO earnPointsBO = new EarnPointsBO();
                        earnPointsBO.setStorename(storeName);
                        earnPointsBO.setPoints(points);
                        earnPointsBO.setStoreId(branchId);
                        //earnPointsBO.setDeviceId(deviceId);

                        storeList.add(earnPointsBO);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                sqLiteDatabase.close();

                adapter = new PointsAdapter(storeList, context, new PointsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        storeName = storeList.get(position).getStorename();
                        branchId = storeList.get(position).getStoreId();
                        Intent i = new Intent(context, StoreHomeActivity.class);
                        //  i.putExtra("storeName", storeName);
                        i.putExtra("branchId", branchId);
                        startActivity(i);
                    }
                });
                recyclerView.setAdapter(adapter);
                // stopping swipe refresh
                refreshLayout.setRefreshing(false);
            }
        }
    }

    private void findViewByAllId() {
        linearLayout = (LinearLayout) findViewById(R.id.ll_retry);
        llErrorMessage = (LinearLayout) findViewById(R.id.ll_points_error_message);
        btnRetry = (Button) findViewById(R.id.btn_retry_points);

        iv_Arrow_Back = (ImageView) findViewById(R.id.your_points_back_arrow);
        ivSyncData = (ImageView) findViewById(R.id.iv_sync_data);
        tvOffline = (TextView) findViewById(R.id.tv_online_offline);
        pb = (ProgressBar) findViewById(R.id.pb_points);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.points_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if (checkInternet.isInternetConnected(getApplicationContext())){
                    retrieveDataFromServer();
                    tvOffline.setText("Online");
                    return;
                }else {
                    retrieveDataFromSQLite();
                    tvOffline.setText("Offline");
                }
            }
        });

        recyclerView  = (RecyclerView) findViewById(R.id.recylerview_your_points);

        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        iv_Arrow_Back.setOnClickListener(this);
        ivSyncData.setOnClickListener(this);
        btnRetry.setOnClickListener(this);

        if (checkInternet.isInternetConnected(context)){
            retrieveDataFromServer();
            tvOffline.setText("Online");
            return;
        }else {
            retrieveDataFromSQLite();
            tvOffline.setText("Offline");
            //return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkInternet.isInternetConnected(this)){
//            retrieveDataFromServer();
//            tvOffline.setText("Online");
            return;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.your_points_back_arrow){
            finish();
        }else if (id == R.id.iv_sync_data){
          // startActivity(new Intent(this, StoreHomeActivity.class));
        }else if (v == btnRetry){
            retrieveDataFromServer();
            storeList.clear();
            pb.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
    }
}
