package in.bizzmark.smartpoints_user.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
    private FirebaseAuth firebaseAuth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        // find all ids
        findViewByAllId();

        firebaseAuth = FirebaseAuth.getInstance();
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

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                etResetPassword.setText("");
                                Toast.makeText(ResetPasswordActivity.this, "Password reset link send successfully Please check your email ", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                generateRandomPassword();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Oops! sending error! please enter correct email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }

    private void generateRandomPassword() {
//        function () {
//            var possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?_-";
//            var password = "";
//            for(var i = 0; i < 16; i += 1) {
//                password += possibleChars[Math.floor(Math.random() * possibleChars.length)];
//            }
//            return password;
//        }
    }
}
