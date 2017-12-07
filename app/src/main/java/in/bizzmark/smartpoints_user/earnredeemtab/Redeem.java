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
import in.bizzmark.smartpoints_user.login.LoginActivity;
import in.bizzmark.smartpoints_user.wifidirect.WiFiDirectActivity;

import static in.bizzmark.smartpoints_user.NavigationActivity.ACCESS_TOKEN;
import static in.bizzmark.smartpoints_user.NavigationActivity.device_Id;

public class Redeem extends Fragment {

    EditText et_redeem_Billamount, etRedeemPoints;
    String type = "redeem";
    Button btnRedeem;
    public static String redeem_Billamount,redeem_points,storeName;
    String point;
    String deviceId = device_Id;

    public Redeem(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.redeem,container,false);

        // retrieve storeName from sharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences("MY_STORE_NAME", Context.MODE_PRIVATE);
        storeName = sp.getString("key_store_name", "");

        et_redeem_Billamount  = (EditText) v.findViewById(R.id.et_redeem_billAmountText);
        etRedeemPoints  = (EditText) v.findViewById(R.id.et_redeem_points);
        btnRedeem = (Button)v. findViewById(R.id.btn_redeem_send);
        /*btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeem_Billamount = et_redeem_Billamount.getText().toString().trim();
                redeem_points = etRedeemPoints.getText().toString().trim();

                if (TextUtils.isEmpty(redeem_Billamount)){
                    Snackbar.make(v,"Please enter bill amount", Snackbar.LENGTH_SHORT).show();
                }else if (redeem_Billamount.startsWith("0")) {
                    Toast.makeText(getActivity(), "amount shouldn't be " + redeem_Billamount, Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(redeem_points)){
                    Snackbar.make(v,"Please enter points", Snackbar.LENGTH_SHORT).show();
                }else if (redeem_points.startsWith("0")) {
                    Toast.makeText(getActivity(), "amount shouldn't be " + redeem_points, Toast.LENGTH_SHORT).show();
                } else{
                    checkDeviceSupportWifiDirect();
                }
            }
        });*/
        return v;
    }


    // Check wifi-direct support
    private boolean checkDeviceSupportWifiDirect() {
        PackageManager pm = getActivity().getPackageManager();
        FeatureInfo features[] = pm.getSystemAvailableFeatures();
        for (FeatureInfo info : features) {
            if (info != null && info.name != null && info.name.equalsIgnoreCase("android.hardware.wifi.direct")){
                    if (!ACCESS_TOKEN.isEmpty()) {
                        sendDataToWifiDirectClass();
                    } else {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                return true;
            }
        }
        Toast.makeText(getActivity(), "Your device is not support wifi-direct", Toast.LENGTH_SHORT).show();
        return false;
    }

    // Send data into Wifi-Direct class
    private void sendDataToWifiDirectClass() {
        point = redeem_points;
//        Intent i = new Intent(getContext(), WiFiDirectActivity.class);
//        startActivity(i);
//        getActivity().finish();

        // get current time of device
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String timeDate = s.format(calendar.getTime());

        PointsBO pointsBO = new PointsBO();
        pointsBO.setBillAmount(redeem_Billamount);
        pointsBO.setStoreName(storeName);
        pointsBO.setType(type);
        pointsBO.setPoints(point);
        pointsBO.setTime(timeDate);
        pointsBO.setDeviceId(deviceId);

        Gson gson = new Gson();
        String jsonEarn = gson.toJson(pointsBO);

        // save jsonData in sharedPreferences
        SharedPreferences.Editor editor = getActivity().
                getSharedPreferences("MY_BILL_AMOUNT", Context.MODE_PRIVATE).edit();
        editor.putString("key_bill_amount", jsonEarn);
        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        retrievingAccessTokenFromSP();

        et_redeem_Billamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    redeem_Billamount = s.toString();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

        etRedeemPoints.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    redeem_points = s.toString();
                    saveString(redeem_Billamount, redeem_points);
                   /* if (TextUtils.isEmpty(redeem_points)) {
                        Toast.makeText(getActivity(), "Points required", Toast.LENGTH_SHORT).show();
                    }else if (redeem_Billamount.startsWith("0")) {
                        Toast.makeText(getActivity(), "amount shouldn't be " + redeem_Billamount, Toast.LENGTH_SHORT).show();
                    }else {
                        checkDeviceSupportWifiDirect();
                    }*/
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveString(String redeem_billamount, String redeemPoints) {
        try {
            if (redeem_billamount.startsWith("0") && redeemPoints.startsWith("0")) {
                Toast.makeText(getActivity(), "shouldn't be " + redeem_Billamount+"\n"+ redeemPoints , Toast.LENGTH_SHORT).show();
            }else {
                checkDeviceSupportWifiDirect();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        retrievingAccessTokenFromSP();
    }

    // retrieve access-token from sharedPreferences
    private void retrievingAccessTokenFromSP() {
        SharedPreferences sp = getActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        ACCESS_TOKEN = sp.getString("access_token", "");
    }
}