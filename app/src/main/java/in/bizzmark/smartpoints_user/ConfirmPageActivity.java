package in.bizzmark.smartpoints_user;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.bizzmark.smartpoints_user.wifidirect.FileTransferService;

/**
 * Created by User on 16-Mar-17.
 */
public class ConfirmPageActivity extends Activity implements View.OnClickListener {

    private ImageView iv_Back;
    private TextView tvEarnAmount,tvError;
    private Button btnCancel,btnConfirm,btnEdit;
    private EditText etEditAmount;

    String bill_amount,billa_amount_edit;

    private WifiP2pInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);



        allIds();
        allOnclick();
    }

    private void allOnclick() {
        iv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }


    private void allIds() {
        iv_Back = (ImageView) findViewById(R.id.iv_back_arrow_confirm_page);
        tvEarnAmount = (TextView) findViewById(R.id.tv_confirm_earn_amount);
        tvError = (TextView) findViewById(R.id.tv_error_message);
        btnCancel = (Button) findViewById(R.id.btn_cancel_confirm_page);
        btnConfirm = (Button) findViewById(R.id.btn_confirm_amount);
        btnEdit = (Button) findViewById(R.id.btn_edit_confirm_page);
        etEditAmount = (EditText) findViewById(R.id.et_edit_amount);

        SharedPreferences sp = getSharedPreferences("MY_BILL_AMOUNT", MODE_PRIVATE);
        bill_amount = sp.getString("key_bill_amount","amount");
        tvEarnAmount.setText("Rs : " + bill_amount);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_cancel_confirm_page){
            startActivity(new Intent(this,NavigationActivity.class));
            finish();
        }else if (id == R.id.btn_confirm_amount){
            sendAmount();
        }else if (id == R.id.btn_edit_confirm_page){
            etEditAmount.setVisibility(View.VISIBLE);
            etEditAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    bill_amount = s.toString();
                    tvEarnAmount.setText("Rs : " + bill_amount);
                    tvError.setVisibility(View.GONE);
                }
            });







        }
    }

    private void sendAmount() {

        if (TextUtils.isEmpty(bill_amount)){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Empty field amount required");
        }else {

              Toast.makeText(this, "Rs : " +bill_amount, Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this, WiFiDirectActivity.class);
//            startActivity(intent);

//            SharedPreferences.Editor editor = this.
//                    getSharedPreferences("MY_BILL_AMOUNT", Context.MODE_PRIVATE).edit();
//            editor.putString("key_bill_amount", bill_amount);
//            editor.commit();

        Intent serviceIntent = new Intent(getApplication(), FileTransferService.class);
        serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
        serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS, info.groupOwnerAddress.getHostAddress());
        serviceIntent.putExtra(FileTransferService.MESSAGE, bill_amount);
        serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8888);
        getApplicationContext().startService(serviceIntent);

            finish();
        }
    }
}
