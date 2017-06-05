package in.bizzmark.smartpoints_user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static in.bizzmark.smartpoints_user.NavigationActivity.ACCESS_TOKEN;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    int CAMERA_REQUEST_CODE = 100;
    int GALLERY_REQUEST_CODE = 101;

    ImageView iv_back_arrow_edit_profile;
    ImageView iv_Profile_pic;
    TextView editProfile;

    EditText etName, etEmail, etMobile, etDob;
    Button btnUpdate, btnLogout;

    boolean status = true;
    String name, email, mobile, dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Find All Ids
        findViewByAllIds();

        // RETRIEVING DETAILS FROM SP
        retrievingDataFromSp();
    }

    // RETRIEVING DETAILS FROM SP
    private void retrievingDataFromSp() {
        SharedPreferences sp = getApplication().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        name = sp.getString("name", "");
        email = sp.getString("email", "");
        mobile = sp.getString("mobile", "");

        etName.setText(name);
        etEmail.setText(email);
        etMobile.setText(mobile);
        etDob.setText(dob);
    }

    private void findViewByAllIds() {
        iv_Profile_pic = (ImageView) findViewById(R.id.circleView_profile_pic);
        iv_back_arrow_edit_profile = (ImageView) findViewById(R.id.back_arrow_edit_profile);
        editProfile = (TextView) findViewById(R.id.profile_pic_edit_tv);
        iv_back_arrow_edit_profile.setOnClickListener(this);
        editProfile.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.et_edit_profile_name);
        etEmail = (EditText) findViewById(R.id.et_edit_profile_email_id);
        etMobile = (EditText) findViewById(R.id.et_edit_profile_mobile_no);
        etDob = (EditText) findViewById(R.id.et_edit_profile_date_of_birth);

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnUpdate.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_back_arrow_edit_profile){
            finish();
        }else if (v == editProfile){
            // for select profile image
            showSelectProfilePicDialog();
        }else if (v == btnUpdate){
            // do update profile
            updateProfileData();
        }else if (v == btnLogout){
            // do logout
            showLogoutDialog();
        }
    }

    // do logout
    private void showLogoutDialog() {
        if (!ACCESS_TOKEN.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you wana logout ?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // Clear data from SharedPreferences
                            SharedPreferences preferences =getSharedPreferences("USER_DETAILS",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setCancelable(false)
                    .create().show();
        }else {
            //  Toast.makeText(this, "You not logged in, please login first", Toast.LENGTH_SHORT).show();
        }
    }

    // Update profile
    private void updateProfileData() {
        Toast.makeText(this, "Under Develop", Toast.LENGTH_SHORT).show();
    }

    // for select profile image
    private void showSelectProfilePicDialog() {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Choose from")
                .setCancelable(true)
                .setPositiveButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImageFromGallery();
                    }
                })
                .setNegativeButton("CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImageFromCamera();
                    }
                }).create().show();
    }

    // Select Image from Gallery
    private void getImageFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,GALLERY_REQUEST_CODE);
    }

    // Get Image-from Camera
    private void getImageFromCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK  && null != data ){
            Uri uri = data.getData();
            //Using Picasso Library
            Picasso.with(this)
                    .load(uri)
                    .into(iv_Profile_pic);

            //View Image in Normal Method
         /*   String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            iv_Profile_pic = (ImageView) findViewById(R.id.circleView_profile_pic);
            iv_Profile_pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/

        }else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK  && null != data ){
            Uri uri = data.getData();
            //Using Picasso Library
            Picasso.with(this)
                    .load(uri)
                    .into(iv_Profile_pic);
        }
    }
}
