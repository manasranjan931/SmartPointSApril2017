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

public class RegisterActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView imageView_back_arrow;
    private TextView tvSignin;
    private TextInputEditText etEmail,etPassword;
    private CheckBox checkBox_Password;
    private Button btnRegister;
    private String email,password;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // find all ids
        findViewByAllId();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void findViewByAllId() {
        imageView_back_arrow = (ImageView) findViewById(R.id.register_back_arrow);
        tvSignin = (TextView) findViewById(R.id.tv_sign_in);
        etEmail = (TextInputEditText) findViewById(R.id.et_email_register);
        etPassword = (TextInputEditText) findViewById(R.id.et_password_register);
        checkBox_Password = (CheckBox) findViewById(R.id.checkbox_password_register);
        btnRegister = (Button) findViewById(R.id.btn_sign_up);

        imageView_back_arrow.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        checkBox_Password.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.register_back_arrow:
                finish();
                break;
            case R.id.btn_sign_up:
                userSignUp(v);
                break;
            case R.id.tv_sign_in:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }

    // USER SIGNUP
    private void userSignUp(View v) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....Registering");

        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check email empty
        if (TextUtils.isEmpty(email)){
            Snackbar.make(v,"Please enter email", Snackbar.LENGTH_SHORT).show();
            // Check email validation
        }else if (!email.matches(emailValidation)){
            Snackbar.make(v,"Invalid email address", Snackbar.LENGTH_SHORT).show();
            // Check email empty
        }else if (TextUtils.isEmpty(password)){
                Snackbar.make(v,"Please enter password", Snackbar.LENGTH_SHORT).show();
        }else if (password.length() < 8 || password.length() > 16){
            Snackbar.make(v,"Password must be 8 to 16 character", Snackbar.LENGTH_SHORT).show();
        }else {
                progressDialog.show();
                // do signup
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                                    etEmail.setText("");
                                    etPassword.setText("");
                                    //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    //finish();
                                    sendEmailVerificationLink();
                                }else {
                                    Toast.makeText(RegisterActivity.this, "registration error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        }
    }

    // Send Email Verification Link
    private void sendEmailVerificationLink() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                finish();
                                Toast.makeText(RegisterActivity.this, "Signup successful. Verification email sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // for Password hide and show
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
