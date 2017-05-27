package in.bizzmark.smartpoints_user.store;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import in.bizzmark.smartpoints_user.R;

import static in.bizzmark.smartpoints_user.database.PointsActivity.branchId;

/**
 * Created by User on 02-May-17.
 */

public class AboutStoreFrag extends Fragment implements View.OnClickListener {

    TextView tvStoreName, tv_branch_name, tv_store_address;
    Button btnCall, btnReward, btnMap;

    String store_details_url = "http://35.154.104.54/smartpoints/customer-api/get-branch-details?branchId="+branchId;
    String storeName,owner_firstname,owner_lastname,owner_mobile,branch_name,branch_address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_store,container,false);
        findAllIds(view);
        return view;
    }

    private void findAllIds(View view) {
        tvStoreName = (TextView) view.findViewById(R.id.tv_storeName);
        tv_branch_name = (TextView) view.findViewById(R.id.tv_branch_name);
        tv_store_address = (TextView) view.findViewById(R.id.tv_store_address);
        btnCall = (Button) view.findViewById(R.id.btn_call_to_store);
        btnReward = (Button) view.findViewById(R.id.btn_);
        btnMap = (Button) view.findViewById(R.id.btn_view_map);

        btnReward.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnMap.setOnClickListener(this);

        dataRetrieveFromServer();
        goToRewardsFrag();
        btnReward.setBackgroundColor(Color.YELLOW);
        btnReward.setTextColor(Color.BLACK);
        btnCall.setBackgroundColor(Color.WHITE);
        btnCall.setTextColor(Color.BLACK);
        btnMap.setBackgroundColor(Color.WHITE);
        btnMap.setTextColor(Color.BLACK);
    }

    private void dataRetrieveFromServer() {
        StringRequest request = new StringRequest(Request.Method.GET, store_details_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                        try {
                            JSONObject jo = new JSONObject(result);
                            JSONObject jo2 = jo.getJSONObject("response");

                                storeName = jo2.getString("store_name");
                                owner_firstname = jo2.getString("owner_firstname");
                                owner_lastname = jo2.getString("owner_lastname");
                                owner_mobile = jo2.getString("owner_mobile");
                                branch_name = jo2.getString("branch_name");
                                branch_address = jo2.getString("branch_address");

                            tvStoreName.setText(storeName);
                            tv_branch_name.setText(branch_name);
                            tv_store_address.setText(branch_address);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "something error, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    @Override
    public void onClick(View v) {
        if (v == btnReward){
            btnReward.setBackgroundColor(Color.YELLOW);
            btnReward.setTextColor(Color.BLACK);
            Toast.makeText(getActivity(), "Reward", Toast.LENGTH_SHORT).show();

            btnCall.setBackgroundColor(Color.WHITE);
            btnCall.setTextColor(Color.BLACK);
            btnMap.setBackgroundColor(Color.WHITE);
            btnMap.setTextColor(Color.BLACK);

            goToRewardsFrag();

        }else if (v == btnCall){
            btnCall.setBackgroundColor(Color.BLUE);
            btnCall.setTextColor(Color.WHITE);

            if (owner_mobile != null) {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + owner_mobile)));
            }else {
                Toast.makeText(getActivity(), "Number not available", Toast.LENGTH_SHORT).show();
            }

            btnReward.setBackgroundColor(Color.WHITE);
            btnReward.setTextColor(Color.BLACK);
            btnMap.setBackgroundColor(Color.WHITE);
            btnMap.setTextColor(Color.BLACK);

            goToCallFrag();

        }else if (v == btnMap){
            btnMap.setBackgroundColor(Color.YELLOW);
            btnMap.setTextColor(Color.BLACK);
            Toast.makeText(getActivity(), "Map", Toast.LENGTH_SHORT).show();

            btnReward.setBackgroundColor(Color.WHITE);
            btnReward.setTextColor(Color.BLACK);
            btnCall.setBackgroundColor(Color.WHITE);
            btnCall.setTextColor(Color.BLACK);

            goToMapFrag();
        }

    }

    private void goToCallFrag() {
        CallFrag callFrag = new CallFrag();

        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_about_store, callFrag, "fragment");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void goToMapFrag() {
        AboutStoreMapFrag aboutStoreMapFrag = new AboutStoreMapFrag();

        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_about_store, aboutStoreMapFrag, "fragment");
        ft.addToBackStack(null);
        ft.commit();
    }

    private void goToRewardsFrag() {
        RewardsFrag rewardsFrag = new RewardsFrag();

        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_about_store, rewardsFrag, "fragment");
        ft.addToBackStack(null);
        ft.commit();
    }
}
