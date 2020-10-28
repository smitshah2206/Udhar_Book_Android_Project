package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class passcode extends AppCompatActivity {

    TextView user_number,error_msg,re_create;
    EditText number1,number2,number3,number4;
    ImageView redirectback,set_passcode;
    String phone_number,get_passcode,passcode,get_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        redirectback = findViewById(R.id.redirectback);
        user_number = findViewById(R.id.user_number);
        set_passcode = findViewById(R.id.set_passcode);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        error_msg = findViewById(R.id.error_msg);
        re_create = findViewById(R.id.re_create);

        number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!number1.getText().toString().isEmpty()) {
                    number2.setFocusableInTouchMode(true);
                    number2.requestFocus();
                }
                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_passcode();
                }
            }
        });
        number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number2.getText().toString().isEmpty()) {
                    number1.setFocusableInTouchMode(true);
                    number1.requestFocus();
                }
                else{
                    number3.setFocusableInTouchMode(true);
                    number3.requestFocus();
                }

                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_passcode();
                }
            }
        });
        number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number3.getText().toString().isEmpty()) {
                    number2.setFocusableInTouchMode(true);
                    number2.requestFocus();
                }
                else{
                    number4.setFocusableInTouchMode(true);
                    number4.requestFocus();
                }

                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_passcode();
                }
            }
        });
        number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number4.getText().toString().isEmpty()) {
                    number3.setFocusableInTouchMode(true);
                    number3.requestFocus();
                }
                if(!number1.getText().toString().isEmpty() && !number2.getText().toString().isEmpty() && !number3.getText().toString().isEmpty() && !number4.getText().toString().isEmpty()){
                    verify_passcode();
                }
            }
        });

        Intent intent = getIntent();
        get_id = intent.getStringExtra("Id");
        get_passcode = intent.getStringExtra("Passcode");
        phone_number = intent.getStringExtra("User_number");
        user_number.setText("+91-"+phone_number.substring(2));
        set_passcode.setOnClickListener(new View.OnClickListener() {
            int count=0;
            @Override
            public void onClick(View view) {

              if(count==0){
                  set_passcode.setImageResource(R.drawable.ic_eye_slash_solid);
                  number1.setTransformationMethod(null);
                  number2.setTransformationMethod(null);
                  number3.setTransformationMethod(null);
                  number4.setTransformationMethod(null);
                  count=1;

              }
              else{
                  set_passcode.setImageResource(R.drawable.ic_eye_solid);
                  number1.setTransformationMethod(new PasswordTransformationMethod());
                  number2.setTransformationMethod(new PasswordTransformationMethod());
                  number3.setTransformationMethod(new PasswordTransformationMethod());
                  number4.setTransformationMethod(new PasswordTransformationMethod());
                  count=0;
              }
            }
        });


        redirectback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(passcode.this, login.class);
                startActivity(new_intent);
                finish();
            }
        });

        re_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(passcode.this, recreate_otp.class);
                intent.putExtra("User_number", phone_number);
                startActivity(intent);
                finish();
            }
        });
    }

    public void verify_passcode() {

        passcode = number1.getText().toString() + number2.getText().toString() + number3.getText().toString() + number4.getText().toString();
        if (passcode.compareTo(get_passcode) == 0) {
                Intent intent = new Intent(passcode.this,dashboard.class);
                SharedPreferences SharedPreferences = getSharedPreferences("UserDetails",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = SharedPreferences.edit();
                myEdit.putBoolean("is_logged_in",true);
                myEdit.putString("Id",get_id);
                myEdit.putString("user_phone_number",phone_number);
                myEdit.putString("user_passcode",get_passcode);
                myEdit.commit();
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
            } else {
                error_msg.setText("Invalid Passcode");
            }
    }


}