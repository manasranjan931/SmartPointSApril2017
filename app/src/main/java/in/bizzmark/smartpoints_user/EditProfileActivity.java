package in.bizzmark.smartpoints_user;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    int CAMERA_REQUEST_CODE = 100;
    int GALLERY_REQUEST_CODE = 101;


    ImageView iv_back_arrow_edit_profile;
    ImageView iv_Profile_pic;
    TextView editProfile;

    boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iv_Profile_pic = (ImageView) findViewById(R.id.circleView_profile_pic);

        iv_back_arrow_edit_profile = (ImageView) findViewById(R.id.back_arrow_edit_profile);
        iv_back_arrow_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editProfile = (TextView) findViewById(R.id.profile_pic_edit_tv);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Select From")
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
});
        }

    private void getImageFromCamera() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_REQUEST_CODE);

    }

    private void getImageFromGallery() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,GALLERY_REQUEST_CODE);


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
