package in.bizzmark.smartpoints_user.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import in.bizzmark.smartpoints_user.R;

import static in.bizzmark.smartpoints_user.NavigationActivity.networkInfo;

public class LoginActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView imageView_back_arrow;
    private TextView tvSignup,tvForgotPassword;

    private TextView tvSkip;

    private EditText etEmail,etPassword;
    private CheckBox checkBox_Password;
    private Button btnLogin;
    private String email,password;
    private ProgressDialog progressDialog;

    private Animation animBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // find all ids
        findViewByAllId();

        // Load the animation
        animBounce = AnimationUtils.loadAnimation(this,R.anim.bounce);
    }

    private void findViewByAllId() {
        imageView_back_arrow = (ImageView) findViewById(R.id.login_back_arrow);
        etEmail = (EditText) findViewById(R.id.et_email_login);
        etPassword = (EditText) findViewById(R.id.et_password_login);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        checkBox_Password = (CheckBox) findViewById(R.id.checkbox_password_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_sign_up);

        tvSkip = (TextView) findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(this);

        imageView_back_arrow.setOnClickListener(this);
        checkBox_Password.setOnCheckedChangeListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
    }


    @Override
    public void onClick(final View v) {

        int i = 0;

        int id = v.getId();
        switch (id){
            case R.id.login_back_arrow:
                finish();
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(this,ResetPasswordActivity.class));
                finish();
                break;
            case R.id.btn_login:
                btnLogin.startAnimation(animBounce);
                if (networkInfo != null) {
                    userLogin(v);
                }else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_sign_up:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
            case R.id.tv_skip:
               // startActivity(new Intent(this,NavigationActivity.class));
                if (i == 0) {
                    tvSkip.setVisibility(View.GONE);
                    finish();
                    tvSkip.startAnimation(animBounce);
                }
                break;
        }
    }

    // for user login
    private void userLogin(View v) {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString().trim();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait loging in.....");

        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check email empty
        if (TextUtils.isEmpty(email)){
            Snackbar.make(v,"Please enter email", Snackbar.LENGTH_SHORT).show();
            // Check email validation
        }else if (!email.matches(emailValidation)){
            Snackbar.make(v,"Invalid email address", Snackbar.LENGTH_SHORT).show();
            // Check password empty
        }else if (TextUtils.isEmpty(password)){
            Snackbar.make(v,"Please enter password", Snackbar.LENGTH_SHORT).show();
        }else if (password.length() < 8 || password.length() > 16){
            Snackbar.make(v,"Password must be 8 to 16 characters", Snackbar.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            // do login
        }
    }

    // Password hide and show
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
