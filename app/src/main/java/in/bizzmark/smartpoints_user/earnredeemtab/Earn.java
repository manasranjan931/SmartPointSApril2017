package in.bizzmark.smartpoints_user.earnredeemtab;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.bo.PointsBO;
import in.bizzmark.smartpoints_user.wifidirect.WiFiDirectActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Earn extends Fragment {

    public static final int REQUEST_READ_PERMISSION = 1;

    String earn_Billamount;
    EditText et_earn_Billamount;
    Button btnEarn;
    String storeName;
    String deviceId;

    public Earn(){
        // Require empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.earn,container,false);
        findViewById(v);

        // retrieve storeName from sharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences("MY_STORE_NAME", Context.MODE_PRIVATE);
        storeName = sp.getString("key_store_name", "");
       // Toast.makeText(getActivity(), "Store Name : "+ storeName, Toast.LENGTH_SHORT).show();
        return v;
    }

    private void findViewById(final View v) {
        et_earn_Billamount = (EditText)v. findViewById(R.id.et_earn_billAmountText);
        btnEarn = (Button)v. findViewById(R.id.btn_earn_send);
        btnEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                earn_Billamount = et_earn_Billamount.getText().toString().trim();
                if (TextUtils.isEmpty(earn_Billamount)) {
                    Snackbar.make(view, "Please enter bill amount", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (!earn_Billamount.startsWith("0")) {

                        // runtime permission getting imei-string
                        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
                        if (!hasPermissionLocation) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    REQUEST_READ_PERMISSION);
                        } else {
                            sendDataToWifiDirectClass();
                        }
                    }else {
                        Toast.makeText(getActivity(), "You enter : "+earn_Billamount, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Send data into wifi Direct-Class
    private void sendDataToWifiDirectClass() {
        // get current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String timeDate = s.format(calendar.getTime());

        int point = Integer.parseInt(earn_Billamount);
        String points = ""+point/10;
        String type = "earn";

        // getting deviceId
        TelephonyManager telephonyManager = (TelephonyManager)
                getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();

        // Save DeviceId Into SharedPreferences
        SharedPreferences.Editor edito = getActivity().
                getSharedPreferences("MY_DEVICE_ID", Context.MODE_PRIVATE).edit();
        edito.putString("deviceid", deviceId);
        edito.commit();

        // set all value in PointBO.class
        PointsBO pointsBO = new PointsBO();
        pointsBO.setBillAmount(earn_Billamount);
        pointsBO.setStoreName(storeName);
        pointsBO.setType(type);
        pointsBO.setPoints(points);
        pointsBO.setTime(timeDate);
        pointsBO.setDeviceId(deviceId);

        // Create Object To Gson
        Gson gson = new Gson();
        String jsonEarn = gson.toJson(pointsBO);

        startActivity(new Intent(getContext(), WiFiDirectActivity.class));

        // save json object into sharedPreferences
        SharedPreferences.Editor editor = getActivity().
                getSharedPreferences("MY_BILL_AMOUNT", Context.MODE_PRIVATE).edit();
        editor.putString("key_bill_amount", jsonEarn);
        editor.commit();
       // Toast.makeText(getContext(), "Json : " + jsonEarn, Toast.LENGTH_SHORT).show();

    }

    // Runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_READ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getActivity(), "Permission granted.", Toast.LENGTH_SHORT).show();
                    sendDataToWifiDirectClass();
                } else
                {
                    Toast.makeText(getActivity(), "The app was not allowed to get your phone state. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }

        }

    }
}