package in.bizzmark.smartpoints_user.earnredeemtab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.bo.PointsBO;
import in.bizzmark.smartpoints_user.login.LoginActivity;
import in.bizzmark.smartpoints_user.wifidirect.WiFiDirectActivity;

import static in.bizzmark.smartpoints_user.earnredeemtab.Earn.REQUEST_READ_PERMISSION;

public class Redeem extends Fragment {

    Spinner spn_RedeemPoints;
    EditText et_redeem_Billamount;
    Button btnRedeem;
    String redeem_Billamount,redeem_points,storeName;
    String point;
    FirebaseAuth firebaseAuth;

    public Redeem(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.redeem,container,false);

        firebaseAuth = FirebaseAuth.getInstance();

        spn_RedeemPoints = (Spinner) v.findViewById(R.id.spinner_select_redeem_points);

        // get data from string value
        final Resources resources = getResources();
        final String points[] = resources.getStringArray(R.array.redeem_points);

        ArrayAdapter aa = new ArrayAdapter(getContext(), R.layout.custom_spinner, R.id.tv_spinner_item,points);
        spn_RedeemPoints.setAdapter(aa);

        spn_RedeemPoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                redeem_points = spn_RedeemPoints.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // retrieve storeName from sharedPreferences
        SharedPreferences sp = getActivity().getSharedPreferences("MY_STORE_NAME", Context.MODE_PRIVATE);
        storeName = sp.getString("key_store_name", "");

        et_redeem_Billamount  = (EditText) v.findViewById(R.id.et_redeem_billAmountText);
        btnRedeem = (Button)v. findViewById(R.id.btn_redeem_send);
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeem_Billamount = et_redeem_Billamount.getText().toString().trim();

                if (TextUtils.isEmpty(redeem_Billamount)){
                    Snackbar.make(v,"Please enter bill amount", Snackbar.LENGTH_SHORT).show();
                }else {
                    if (!redeem_Billamount.startsWith("0")){
                        if (firebaseAuth.getCurrentUser() != null) {

                        if (redeem_points.equals("select points")) {
                            Toast.makeText(getActivity(), "Please select points", Toast.LENGTH_SHORT).show();
                        } else {
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
                        }
                    }else {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                    }else {
                        Toast.makeText(getActivity(), "You enter : "+redeem_Billamount, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return v;
    }

    // Send data into Wifi-Direct class
    private void sendDataToWifiDirectClass() {
        point = redeem_points;
        Intent i = new Intent(getContext(), WiFiDirectActivity.class);
        startActivity(i);

        // get current time of device
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String timeDate = s.format(calendar.getTime());

        // getting deviceId
        TelephonyManager telephonyManager = (TelephonyManager)
                getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        String type = "redeem";

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