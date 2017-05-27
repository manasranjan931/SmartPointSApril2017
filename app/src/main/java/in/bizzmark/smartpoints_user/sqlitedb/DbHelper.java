package in.bizzmark.smartpoints_user.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 28-Mar-17.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "CUSTOMER_DATA";
    public static final int VERSION = 1;

    // Earn Table
    public static final String TABLE_EARN = "CUSTOMER_EARN";

    public static final String STORE_NAME_COL_1 = "STORE_NAME";
    public static final String BILL_AMOUNT_COL_2 = "BILL_AMOUNT";
    public static final String EARN_POINTS_COL_3 = "POINTS";
    public static final String TYPE_COL_4 = "TYPE";
    public static final String DATE_TIME_COL_5 = "DATE_TIME";
    public static final String DEVICE_ID_COL_6 = "DEVICE_ID";

    // Redeem Table
    public static final String TABLE_REDEEM = "CUSTOMER_REDEEM";

    public static final String STORE_NAMES_COL_1 = "STORE_NAME";
    public static final String NEW_BILL_AMOUNTS_COL_2 = "NEW_BILL_AMOUNT";
    public static final String REDEEM_POINTS_COL_3 = "POINTS";
    public static final String TYPES_COL_4 = "TYPE";
    public static final String DATE_TIMES_COL_5 = "DATE_TIME";
    public static final String DEVICE_IDS_COL_6 = "DEVICE_ID";
    public static final String DISCOUNT_AMOUNTS_COL7 = "DISCOUNT_AMOUNTS";



    Context context;
    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // for earn query
        String earn_qry = "create table " + TABLE_EARN + " ( "+STORE_NAME_COL_1+" TEXT, "+BILL_AMOUNT_COL_2+" TEXT, "+EARN_POINTS_COL_3+" TEXT, "+TYPE_COL_4+" TEXT, "+DATE_TIME_COL_5+" TEXT, "+DEVICE_ID_COL_6+" TEXT)";
        db.execSQL(earn_qry);
     //   Toast.makeText(context, "Table created : "+TABLE_EARN, Toast.LENGTH_LONG).show();

        // for redeem query
        String redeem_qry = "create table " + TABLE_REDEEM + " ( "+STORE_NAMES_COL_1+" TEXT, "+NEW_BILL_AMOUNTS_COL_2+" TEXT, "+REDEEM_POINTS_COL_3+" TEXT, "+TYPES_COL_4+" TEXT, "+DATE_TIMES_COL_5+" TEXT, "+DEVICE_IDS_COL_6+" TEXT,"+DISCOUNT_AMOUNTS_COL7+" TEXT)";
        db.execSQL(redeem_qry);
       // Toast.makeText(context, "Table created : "+ TABLE_REDEEM, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
