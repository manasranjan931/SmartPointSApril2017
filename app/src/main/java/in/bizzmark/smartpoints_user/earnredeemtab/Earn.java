package in.bizzmark.smartpoints_user.earnredeemtab;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import static in.bizzmark.smartpoints_user.NavigationActivity.device_Id;

/**
 * A simple {@link Fragment} subclass.
 */
public class Earn extends Fragment {

    public static final int REQUEST_READ_PERMISSION = 1;

    String earn_Billamount;
    EditText et_earn_Billamount;
    String type = "earn";
    Button btnEarn;
    String storeName;
    String deviceId = device_Id;

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
       /* btnEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                earn_Billamount = et_earn_Billamount.getText().toString().trim();
                if (TextUtils.isEmpty(earn_Billamount)) {
                    Snackbar.make(view, "Please enter bill amount", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (!earn_Billamount.startsWith("0")) {
                        checkDeviceSupportWifiDirect();
                    }else {
                        Toast.makeText(getActivity(), "You enter : "+earn_Billamount, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        et_earn_Billamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    earn_Billamount = s.toString();
                    if (!earn_Billamount.startsWith("0")) {
                        checkDeviceSupportWifiDirect();
                    }else {
                        Toast.makeText(getActivity(), "You enter : "+earn_Billamount, Toast.LENGTH_SHORT).show();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    // Check wifi-direct support
    private boolean checkDeviceSupportWifiDirect() {
        PackageManager pm = getActivity().getPackageManager();
        FeatureInfo features[] = pm.getSystemAvailableFeatures();
        for (FeatureInfo info : features) {
            if (info != null && info.name != null && info.name.equalsIgnoreCase("android.hardware.wifi.direct")){
                sendDataToWifiDirectClass();
                return true;
            }
        }
        Toast.makeText(getActivity(), "Your device is not support wifi-direct", Toast.LENGTH_SHORT).show();
        return false;
    }

    // Send data into wifi Direct-Class
    private void sendDataToWifiDirectClass() {
        // get current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String timeDate = s.format(calendar.getTime());

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
        pointsBO.setTime(timeDate);
        pointsBO.setDeviceId(deviceId);

            // Create Object To Gson
            Gson gson = new Gson();
            String jsonEarn = gson.toJson(pointsBO);

//        startActivity(new Intent(getContext(), WiFiDirectActivity.class));
//        getActivity().finish();

        // save json object into sharedPreferences
        SharedPreferences.Editor editor = getActivity().
                getSharedPreferences("MY_BILL_AMOUNT", Context.MODE_PRIVATE).edit();
        editor.putString("key_bill_amount", jsonEarn);
        editor.commit();
       // Toast.makeText(getContext(), "Json : " + jsonEarn, Toast.LENGTH_SHORT).show();

    }
}