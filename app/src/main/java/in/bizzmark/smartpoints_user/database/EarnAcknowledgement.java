package in.bizzmark.smartpoints_user.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.bo.AcknowledgementBO;
import in.bizzmark.smartpoints_user.sqlitedb.DbHelper;

import static in.bizzmark.smartpoints_user.NavigationActivity.device_Id;

/**
 * Created by User on 26-Mar-17.
 */

public class EarnAcknowledgement extends Activity implements View.OnClickListener{

    ImageView ivCross;
    Button btnSaveData,btnOk,btnCancel;
    TextView tvstoreName,tvcancelMessage,tvamount,tvpoints,tvdate;
    RelativeLayout rlStoreName,rlAmount,rlPoints,rlDate;

    AcknowledgementBO ackBO;
    Gson gson = new Gson();
    SimpleDateFormat simpleDateFormat;
    String dateandtime;
    String imeistring;
    String result;

    String storeName ;
    String billAmount ;
    String points ;
    String type ;
    String deviceId = device_Id;
    String time;
    String status;

    DbHelper mydb;
    SQLiteDatabase db;

    String deviceIdfromsp;
    String store_name_from_sqlite;
    String points_from_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earn_acknowledgement);

        // find All Id
        findViewByAllId();

        // from seller
        Intent i = getIntent();
        result = i.getStringExtra("result");

        ackBO = gson.fromJson(result,AcknowledgementBO.class);
        storeName = ackBO.getStoreName();
        billAmount = ackBO.getBillAmount();
        points = ackBO.getPoints();
        type = ackBO.getType();
        time = ackBO.getTime();
        deviceId = ackBO.getDeviceId();
        status = ackBO.getStatus();

        if("success".equalsIgnoreCase(status)){
            // when seller accepting
            tvstoreName.setText(storeName);
            tvamount.setText(billAmount);
            tvpoints.setText(points);
            tvdate.setText(time);
        }else {
            // when seller not accepting
            tvcancelMessage.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);

            rlStoreName.setVisibility(View.GONE);
            rlAmount.setVisibility(View.GONE);
            rlPoints.setVisibility(View.GONE);
            rlDate.setVisibility(View.GONE);

            btnSaveData.setVisibility(View.GONE);
        }

        // getTimeAndDate();
        Calendar calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        dateandtime = simpleDateFormat.format(calander.getTime());

        // Retrieve Data From SQLite-Database
        retrieveDataFromSQLite();

        // retrieve deviceid from sharedPreferences
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MY_DEVICE_ID", Context.MODE_PRIVATE);
        deviceIdfromsp = sp.getString("deviceid", "");

    }

    // Retrieving data from SQLite-Database
    private void retrieveDataFromSQLite() {
        mydb = new DbHelper(this);
        db = mydb.getWritableDatabase();

        if ( mydb != null) {
            String query = "SELECT STORE_NAME, POINTS FROM CUSTOMER_EARN WHERE TYPE= 'earn'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    store_name_from_sqlite = cursor.getString(0);
                    points_from_sqlite = cursor.getString(1);
                } while (cursor.moveToNext());
            }
        }
    }

    // Find All Ids
    private void findViewByAllId() {
        ivCross = (ImageView) findViewById(R.id.iv_cross);
        tvstoreName = (TextView) findViewById(R.id.tv_store_name);
        tvcancelMessage = (TextView) findViewById(R.id.tv_cancel_message);
        tvamount = (TextView) findViewById(R.id.tv_amount_from_seller);
        tvpoints = (TextView) findViewById(R.id.tv_points_from_seller);
        tvdate = (TextView) findViewById(R.id.tv_date_from_seller);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnSaveData = (Button) findViewById(R.id.btn_save_acknowledgement_data);

        rlStoreName = (RelativeLayout) findViewById(R.id.rl_store_name);
        rlAmount = (RelativeLayout) findViewById(R.id.rl_amount);
        rlPoints = (RelativeLayout) findViewById(R.id.rl_points);
        rlDate = (RelativeLayout) findViewById(R.id.rl_date_time);

        ivCross.setOnClickListener(this);
        btnSaveData.setOnClickListener(this);
        btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_cross){
            finish();
        } else if (id == R.id.btn_ok) {
            finish();
        } else if (id == R.id.btn_save_acknowledgement_data) {
            saveDataIntoSQLite();
            startActivity(new Intent(this,PointsActivity.class));
            finish();
        }
    }

    // SAVE DATA INTO SQLite-DB
    private void saveDataIntoSQLite() {
        if (type.equalsIgnoreCase("earn")) {
          //  Toast.makeText(this, "Earn Type", Toast.LENGTH_SHORT).show();

            // save data into sqlite-database
            mydb = new DbHelper(this);
            db = mydb.getWritableDatabase();

            ContentValues cv = new ContentValues();

            if (store_name_from_sqlite != null && points_from_sqlite != null) {
                if (store_name_from_sqlite.equalsIgnoreCase(storeName)) {

                    int point = Integer.parseInt(points);
                    int sql_point = Integer.parseInt(points_from_sqlite);
                    int totl_points = point+sql_point;
                    String total_points = Integer.toString(totl_points);

                    cv.put(DbHelper.STORE_NAME_COL_1, storeName);
                    cv.put(DbHelper.BILL_AMOUNT_COL_2, billAmount);
                    cv.put(DbHelper.EARN_POINTS_COL_3, total_points);
                    cv.put(DbHelper.TYPE_COL_4, type);
                    cv.put(DbHelper.DATE_TIME_COL_5, dateandtime);
                    cv.put(DbHelper.DEVICE_ID_COL_6, deviceId);

                }else {
                    cv.put(DbHelper.STORE_NAME_COL_1, storeName);
                    cv.put(DbHelper.BILL_AMOUNT_COL_2, billAmount);
                    cv.put(DbHelper.EARN_POINTS_COL_3, points);
                    cv.put(DbHelper.TYPE_COL_4, type);
                    cv.put(DbHelper.DATE_TIME_COL_5, dateandtime);
                    cv.put(DbHelper.DEVICE_ID_COL_6, deviceId);
                }
            }else {
                cv.put(DbHelper.STORE_NAME_COL_1, storeName);
                cv.put(DbHelper.BILL_AMOUNT_COL_2, billAmount);
                cv.put(DbHelper.EARN_POINTS_COL_3, points);
                cv.put(DbHelper.TYPE_COL_4, type);
                cv.put(DbHelper.DATE_TIME_COL_5, dateandtime);
                cv.put(DbHelper.DEVICE_ID_COL_6, deviceId);
            }

            long result = db.insert(DbHelper.TABLE_EARN, null, cv);
            if (result != -1) {
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
               // Toast.makeText(this, "StoreName : "+ storeName +"\n"+ "Points : "+points +"\n"+ "BillAmount : "+billAmount +"\n"+ "Type : "+type +"\n"+ "Date&Time : "+dateandtime +"\n"+ "DeviceId : "+deviceId, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Data saving error : "+result, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String getTimeAndDate() {
        Calendar calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        dateandtime = simpleDateFormat.format(calander.getTime());
        return dateandtime;
    }
}
