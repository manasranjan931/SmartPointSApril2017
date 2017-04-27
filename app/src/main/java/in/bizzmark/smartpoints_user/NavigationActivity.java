package in.bizzmark.smartpoints_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import in.bizzmark.smartpoints_user.database.PointsActivity;
import in.bizzmark.smartpoints_user.login.LoginActivity;

import static android.Manifest.permission.CAMERA;

public class NavigationActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE = 100;

    ImageView imageView_Menu,imageView_Share,imageView_Logout;
    CircleImageView profileImageView;
    TextView tvUserEmail;

    Button btn_Your_Points;
    boolean doubleBackToExitPressedOnce = false;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        firebaseAuth = FirebaseAuth.getInstance();

        imageView_Menu = (ImageView) findViewById(R.id.image_menu);
        imageView_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        imageView_Share = (ImageView) findViewById(R.id.header_share);
        imageView_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EarnRedeemActivity.class));
            }
        });

        imageView_Logout = (ImageView) findViewById(R.id.header_logout);
        imageView_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // code for logout
              logout();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        // for profile picture
        profileImageView = (CircleImageView) navHeader.findViewById(R.id.profile_circleView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(NavigationActivity.this, EditProfileActivity.class));
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });

        // set device id
        tvUserEmail = (TextView) navHeader.findViewById(R.id.userEmail);
      //  tvUserEmail.setText(Scanner_Fragment.DEVICE_ID);

        // for points button
        btn_Your_Points = (Button) findViewById(R.id.your_points_button);
        btn_Your_Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(NavigationActivity.this, PointsActivity.class));

            }
        });

        // Request Camera Permission
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (checkPermissions()){
           // Toast.makeText(getApplicationContext(), "Camera Permission already granted", Toast.LENGTH_LONG).show();
        }else {
            requestPermission();
        }
    }

    private boolean checkPermissions() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                    if (shouldShowRequestPermissionRationale(CAMERA)){
                        new AlertDialog.Builder(NavigationActivity.this)
                                .setMessage("You haven't given us permission to use camera, please enable the permission in setting to start scanning SmartpointS code")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA},REQUEST_CODE);
                                    }
                                }).setCancelable(false)
                                .create()
                                .show();
                    }
                }
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // logout user
    private void logout() {
        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
            Toast.makeText(NavigationActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(NavigationActivity.this, "Please signin first", Toast.LENGTH_SHORT).show();
        }
    }

    // backpressed click
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to close SmartPoints", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_faq)
        {
            startActivity(new Intent(NavigationActivity.this,FAQActivity.class));
        }
        else if (id == R.id.nav_terms_conditions)
        {
            startActivity(new Intent(NavigationActivity.this,TermCondtionActivity.class));
        }
        else if (id == R.id.nav_privacy_policy)
        {
            startActivity(new Intent(NavigationActivity.this,PrivacyPolicyActivity.class));
        }
        else if (id == R.id.nav_share)
        {
        }
        else if (id == R.id.nav_local_store) {
           // startActivity(new Intent(NavigationActivity.this,LocalStoreActivity.class));
        }
        else if (id == R.id.nav_contact_us)
        {
        }
        else if (id == R.id.nav_logout){
            // code for logout
            logout();
        }
        else if (id == R.id.nav_exit)
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            super.onBackPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
