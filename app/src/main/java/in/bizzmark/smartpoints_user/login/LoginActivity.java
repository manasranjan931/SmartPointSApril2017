package in.bizzmark.smartpoints_user.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.bizzmark.smartpoints_user.R;

public class LoginActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView imageView_back_arrow;
    private TextView tvSignup,tvForgotPassword;
    private TextInputEditText etEmail,etPassword;
    private CheckBox checkBox_Password;
    private Button btnLogin;
    private String email,password;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // find all ids
        findViewByAllId();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void findViewByAllId() {
        imageView_back_arrow = (ImageView) findViewById(R.id.login_back_arrow);
        etEmail = (TextInputEditText) findViewById(R.id.et_email_login);
        etPassword = (TextInputEditText) findViewById(R.id.et_password_login);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        checkBox_Password = (CheckBox) findViewById(R.id.checkbox_password_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_sign_up);

        imageView_back_arrow.setOnClickListener(this);
        checkBox_Password.setOnCheckedChangeListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
    }


    @Override
    public void onClick(final View v) {

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
                userLogin(v);
                break;
            case R.id.tv_sign_up:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
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
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                etEmail.setText("");
                                etPassword.setText("");
                                finish();
                            }else {
                                checkEmailVerified();
                                etPassword.setText("");
                                Toast.makeText(LoginActivity.this, "EMAIL AND PASSWORD NOT MATCHING", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void checkEmailVerified() {
       FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null ) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(LoginActivity.this, "User is signed in and email is verified", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        };
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
