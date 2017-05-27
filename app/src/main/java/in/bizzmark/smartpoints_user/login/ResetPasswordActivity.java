package in.bizzmark.smartpoints_user.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.bizzmark.smartpoints_user.R;

/**
 * Created by User on 08-Apr-17.
 */

public class ResetPasswordActivity extends Activity implements View.OnClickListener {
    private LinearLayout llError;
    private TextView tvErrorMessage;
    private TextInputEditText etResetPassword;
    private Button btnSubmit;
    private String email;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        // find all ids
        findViewByAllId();
    }

    private void findViewByAllId() {
        llError = (LinearLayout) findViewById(R.id.ll_error);
        tvErrorMessage = (TextView) findViewById(R.id.tv_empty_error);
        etResetPassword = (TextInputEditText) findViewById(R.id.et_password_reset);
        btnSubmit = (Button) findViewById(R.id.btn_reset_password_submit);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == btnSubmit) {
            sendResetLink(v);
        }
    }

    private void sendResetLink(View v) {
        etResetPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = s.toString();
                llError.setVisibility(View.GONE);
                tvErrorMessage.setVisibility(View.GONE);
            }
        });

        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check empty field
        if (TextUtils.isEmpty(email)){
            llError.setVisibility(View.VISIBLE);
            tvErrorMessage.setVisibility(View.VISIBLE);
        }else if (!email.matches(emailValidation)){
            llError.setVisibility(View.VISIBLE);
            tvErrorMessage.setVisibility(View.VISIBLE);
            tvErrorMessage.setText("Invalid email address. Please enter valid email!");
        }else {
            // code for reset password
            pd = new ProgressDialog(this);
            pd.setMessage("Please wait....");
            pd.show();
        }
    }
}
