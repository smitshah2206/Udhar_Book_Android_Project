package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_SCREEN = 3000;
    ImageView app_icon;
    TextView app_name;
    Animation top_anim,bottom_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        app_icon = findViewById(R.id.app_icon);
        app_name = findViewById(R.id.app_name);



        top_anim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        app_icon.setAnimation(top_anim);
        app_name.setAnimation(bottom_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                File f = new File("/data/data/" + getPackageName() +  "/shared_prefs/UserDetails.xml");

                if (f.exists()){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                    Boolean is_logged_in = sharedPreferences.getBoolean("is_logged_in", true);
                    if (is_logged_in) {

                        Intent intent = new Intent(getApplicationContext(), dashboard.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Boolean first_time_login = sharedPreferences.getBoolean("first_time_login", true);
                        if (first_time_login) {

                            Dexter.withContext(getApplicationContext()).withPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_SMS).withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report)
                                {

                                }
                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                                {
                                    token.continuePermissionRequest();
                                }
                            }).check();

                            Intent intent = new Intent(getApplicationContext(), registration.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), onbording.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_SCREEN);
    }



}