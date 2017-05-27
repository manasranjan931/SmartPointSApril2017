package in.bizzmark.smartpoints_user.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.bo.AcknowledgementBO;
import in.bizzmark.smartpoints_user.sqlitedb.DbHelper;

/**
 * Created by User on 26-Apr-17.
 */

public class RedeemAcknowledgement extends Activity implements View.OnClickListener {
    private TextView tvStoreName,tvNewAmount,tvPoints,tvDiscountAmount;
    private TextView tvcancelMessage;
    private Button btnOk,btnSaveData;

    private RelativeLayout rlStoreName,rlAmount,rlPoints,rlDate;

    AcknowledgementBO ackBO;
    Gson gson = new Gson();
    String dateandtime;
    String imeistring;
    String result;

    String storeName ;
    String billAmount ;
    String points ;
    String type ;
    String discountAmount;
    String newBillAmount;
    String deviceId = imeistring;
    String time;
    String status;

    DbHelper mydb;
    SQLiteDatabase db;

    String store_name_from_sqlite;
    String points_from_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_acknowledgement);

        // find All Id
        findViewByAllId();

        // Retrieve Data From SQLite-Database
        //retrieveDataFromSQLite();

        // from seller
        Intent i = getIntent();
        result = i.getStringExtra("result");

        ackBO = gson.fromJson(result,AcknowledgementBO.class);
        storeName = ackBO.getStoreName();
        billAmount = ackBO.getBillAmount();
        points = ackBO.getPoints();
        type = ackBO.getType();
        time = ackBO.getTime();
        discountAmount = ackBO.getDisCountAmount();
        newBillAmount = ackBO.getNewBillAmount();
        deviceId = ackBO.getDeviceId();
        status = ackBO.getStatus();

        if("success".equalsIgnoreCase(status)){
            // when seller accepting
            tvStoreName.setText(storeName);
            tvNewAmount.setText(newBillAmount);
            tvPoints.setText(discountAmount);
            tvDiscountAmount.setText(discountAmount);
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

    private void findViewByAllId() {
        tvStoreName = (TextView) findViewById(R.id.tv_store_name_redeem);
        tvNewAmount = (TextView) findViewById(R.id.tv_new_amount_from_seller_redeem);
        tvPoints = (TextView) findViewById(R.id.tv_redeem_points_from_seller);
        tvDiscountAmount = (TextView) findViewById(R.id.tv_discount_amount_from_seller_redeem);
        tvcancelMessage = (TextView) findViewById(R.id.tv_cancel_message);

        rlStoreName = (RelativeLayout) findViewById(R.id.rl_store_name);
        rlAmount = (RelativeLayout) findViewById(R.id.rl_amount);
        rlPoints = (RelativeLayout) findViewById(R.id.rl_points);
        rlDate = (RelativeLayout) findViewById(R.id.rl_date_time);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnSaveData = (Button) findViewById(R.id.btn_save_acknowledgement_data);

        btnOk.setOnClickListener(this);
        btnSaveData.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnOk){
            finish();
        }else if (v == btnSaveData){
            saveDataIntoSQLiteDatabase();
            startActivity(new Intent(this,PointsActivity.class));
            finish();
        }
    }

    private void saveDataIntoSQLiteDatabase() {
        if (type.equalsIgnoreCase("redeem")) {
            //  Toast.makeText(this, "Redeem Type", Toast.LENGTH_SHORT).show();

            // save data into sqlite-database
            DbHelper mydb = new DbHelper(this);
            SQLiteDatabase db = mydb.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(DbHelper.STORE_NAMES_COL_1, storeName);
            cv.put(DbHelper.NEW_BILL_AMOUNTS_COL_2, billAmount);
            cv.put(DbHelper.REDEEM_POINTS_COL_3, points);
            cv.put(DbHelper.TYPES_COL_4, type);
            cv.put(DbHelper.DATE_TIMES_COL_5, dateandtime);
            cv.put(DbHelper.DEVICE_IDS_COL_6, deviceId);
            cv.put(DbHelper.DISCOUNT_AMOUNTS_COL7,discountAmount);

            long result = db.insert(DbHelper.TABLE_REDEEM, null, cv);
            if (result != -1) {
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Data saving error : "+result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
