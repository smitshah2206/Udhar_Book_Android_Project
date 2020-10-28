package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class formdashboard extends AppCompatActivity {

    TextView user_name, user_businessname, user_location, user_number;
    String phone_number, name, businessname, location, passcode;
    ImageView redirect, user_image;
    final int REQUEST_CODE_GALLERY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formdashboard);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user_name = findViewById(R.id.user_name);
        user_businessname = findViewById(R.id.user_businessname);
        user_location = findViewById(R.id.user_location);
        user_number = findViewById(R.id.user_number);
        redirect = findViewById(R.id.redirect);
        user_image = findViewById(R.id.user_image);

        phone_number = getIntent().getStringExtra("User_number");
        passcode = getIntent().getStringExtra("Passcode");
        user_number.setText("+91-" + phone_number.substring(2));

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = user_name.getText().toString();
                if (name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                    Resources res = getResources();
                    String uri = "@drawable/" + name.substring(0, 1).toLowerCase();
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable drawable = res.getDrawable(imageResource);
                    user_image.setImageDrawable(drawable);
                }
            }
        });

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = user_name.getText().toString();
                businessname = user_businessname.getText().toString();
                location = user_location.getText().toString();
                if (!name.isEmpty() && !businessname.isEmpty() && !location.isEmpty() && name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$") && location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                    user_name.setError(null);
                    user_businessname.setError(null);
                    user_location.setError(null);
                    Resources res = getResources();
                    String uri = "@drawable/" + name.substring(0, 1).toLowerCase();
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable drawable = res.getDrawable(imageResource);
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] image = stream.toByteArray();
                    DatabaseHelper myDB = new DatabaseHelper(formdashboard.this);
                    if (myDB.storeNewUserData(phone_number, name, passcode, businessname, location, image)) {
                        SharedPreferences SharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = SharedPreferences.edit();
                        myEdit.putBoolean("first_time_login", false);
                        myEdit.putBoolean("is_logged_in", false);
                        myEdit.commit();
                        Toast.makeText(formdashboard.this, "Account Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(formdashboard.this, login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(formdashboard.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (name.isEmpty()) {
                        user_name.setError("Field cannot be empty");
                    } else if (!name.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_name.setError("Require Character and Whitespace Only");
                    } else {
                        user_name.setError(null);
                    }
                    if (businessname.isEmpty()) {
                        user_businessname.setError("Field cannot be empty");
                    } else if (!businessname.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_businessname.setError("Require Character and Whitespace Only");
                    } else {
                        user_businessname.setError(null);
                    }
                    if (location.isEmpty()) {
                        user_location.setError("Field cannot be empty");
                    } else if (!location.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                        user_location.setError("Require Character and Whitespace Only");
                    } else {
                        user_location.setError(null);
                    }
                }

            }
        });

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityCompat.requestPermissions(formdashboard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                Toast.makeText(getApplicationContext(), "Feature Working Progress", Toast.LENGTH_SHORT).show();

            }
        });

        Places.initialize(getApplicationContext(), String.valueOf(R.string.map_key));
        /*user_location.setFocusable(false);
        user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(getApplicationContext());
                startActivityForResult(intent,100);
            }
        });*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                user_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            user_location.setText(place.getName());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}