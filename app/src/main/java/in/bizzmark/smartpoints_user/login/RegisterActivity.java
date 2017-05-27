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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import in.bizzmark.smartpoints_user.R;

import static in.bizzmark.smartpoints_user.NavigationActivity.networkInfo;

public class RegisterActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView imageView_back_arrow;
    private TextView tvSignin;
    private EditText etName, etEmail, etPassword, etConfirmPassword, etMobile, etDOB, etCity;
    private Spinner spnGender;
    private CheckBox checkBox_Password, checkBox_Confirm_Password, cb_Terms_Conditions;
    private Button btnRegister;
    private String name, email, password, confirmPassword, mobile, dob, city, gender;
    private ProgressDialog progressDialog;

    private boolean checkDOBFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // find all ids
        findViewByAllId();
    }

    private void findViewByAllId() {
        imageView_back_arrow = (ImageView) findViewById(R.id.register_back_arrow);
        tvSignin = (TextView) findViewById(R.id.tv_sign_in);

        etName = (EditText) findViewById(R.id.et_person_name);
        etEmail = (EditText) findViewById(R.id.et_email_register);
        etPassword = (EditText) findViewById(R.id.et_password_register);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password_register);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etDOB = (EditText) findViewById(R.id.et_dob);
        etCity = (EditText) findViewById(R.id.et_city);

        spnGender = (Spinner) findViewById(R.id.spgender);

        checkBox_Password = (CheckBox) findViewById(R.id.checkbox_password_register);
        checkBox_Confirm_Password = (CheckBox) findViewById(R.id.checkbox_confirm_password_register);
        cb_Terms_Conditions = (CheckBox) findViewById(R.id.cb_terms_conditions);

        btnRegister = (Button) findViewById(R.id.btn_sign_up);

        imageView_back_arrow.setOnClickListener(this);
        tvSignin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        checkBox_Password.setOnCheckedChangeListener(this);
        checkBox_Confirm_Password.setOnCheckedChangeListener(this);
        cb_Terms_Conditions.setOnCheckedChangeListener(this);

        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = spnGender.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.register_back_arrow:
                finish();
                break;
            case R.id.btn_sign_up:
                if (networkInfo != null || networkInfo.isConnected()) {
                    userSignUp(v);
                }else {
                    Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_sign_in:
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }

    // USER SIGNUP
    private void userSignUp(View v) {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etConfirmPassword.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        dob = etDOB.getText().toString().trim();
        city = etCity.getText().toString().trim();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....Registering");

        String emailValidation = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String dobValidation = "([0-9]{2})/([0-9]{2})/([0-9]{4})";

        if (TextUtils.isEmpty(name)){
            Snackbar.make(v,"Please enter your name", Snackbar.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(email)){
            Snackbar.make(v,"Please enter your email", Snackbar.LENGTH_SHORT).show();
        }else if (!email.matches(emailValidation)){
            Snackbar.make(v,"Invalid email address", Snackbar.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Snackbar.make(v,"Please enter password", Snackbar.LENGTH_SHORT).show();
        }else if (password.length() < 8 || password.length() > 16){
            Snackbar.make(v,"Password must be 8 to 16 character", Snackbar.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(confirmPassword)){
            Snackbar.make(v,"Please enter confirm password", Snackbar.LENGTH_SHORT).show();
        }else if (!password.equalsIgnoreCase(confirmPassword)) {
            Snackbar.make(v,"you entered password do not match", Snackbar.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(mobile)){
            Snackbar.make(v,"Please enter your mobile number", Snackbar.LENGTH_SHORT).show();
        }else if (mobile.length() < 10){
            Snackbar.make(v,"Please enter 10 digits mobile number", Snackbar.LENGTH_SHORT).show();
        }else if (gender.equalsIgnoreCase("Select gender")){
            Snackbar.make(v,"Please select your gender", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(dob)){
            Snackbar.make(v,"Please enter date of birth", Snackbar.LENGTH_SHORT).show();
        }else if (!dob.matches(dobValidation)){
            Snackbar.make(v,"Please enter valid date of birth", Snackbar.LENGTH_SHORT).show();
            checkDOBFormat = false;
        } else if (TextUtils.isEmpty(city)) {
            Snackbar.make(v,"Please enter your city", Snackbar.LENGTH_SHORT).show();
        }else if (!cb_Terms_Conditions.isChecked()){
            Snackbar.make(v,"Please accept term and condition", Snackbar.LENGTH_SHORT).show();
        }else {
            //progressDialog.show();
            // do signup
            Toast.makeText(this, "Your details : " + "\n" + "Name : " + name + "\n" + "Email : " + email + "\n" + "Password : " + password + "\n" + "Confirm Password : " + confirmPassword + "\n" + "Mobile : " + mobile + "\n" + "Gender : " + gender + "\n" + "DOB : " + dob +"\n"+ "City : " + city, Toast.LENGTH_LONG).show();
        }
    }

    // for Password hide and show
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBox_Password) {
            if (isChecked) {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else if (buttonView == checkBox_Confirm_Password){
            if (isChecked) {
                etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }else if (buttonView == cb_Terms_Conditions){
            // Do something for accept term and condition
            if (isChecked){

            }else {

            }
        }
    }
}
