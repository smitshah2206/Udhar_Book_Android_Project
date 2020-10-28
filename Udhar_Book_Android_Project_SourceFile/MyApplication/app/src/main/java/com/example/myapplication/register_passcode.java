package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class register_passcode extends AppCompatActivity {

    String phone_number;
    EditText number1,number2,number3,number4,number5,number6,number7,number8;
    ImageView redirect,set_passcode,re_passcode;
    TextView error_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_passcode);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        redirect = findViewById(R.id.redirectback);
        error_msg = findViewById(R.id.error_msg);
        set_passcode = findViewById(R.id.set_passcode);
        re_passcode = findViewById(R.id.re_passcode);

        phone_number = getIntent().getStringExtra("User_number");

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
                else {
                    number5.setFocusableInTouchMode(true);
                    number5.requestFocus();
                }

            }
        });

        number5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number5.getText().toString().isEmpty()) {
                    number4.setFocusableInTouchMode(true);
                    number4.requestFocus();
                }else{
                    number6.setFocusableInTouchMode(true);
                    number6.requestFocus();
                }
            }
        });
        number6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number6.getText().toString().isEmpty()) {
                    number5.setFocusableInTouchMode(true);
                    number5.requestFocus();
                }
                else{
                    number7.setFocusableInTouchMode(true);
                    number7.requestFocus();
                }

            }
        });
        number7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number7.getText().toString().isEmpty()) {
                    number6.setFocusableInTouchMode(true);
                    number6.requestFocus();
                }
                else{
                    number8.setFocusableInTouchMode(true);
                    number8.requestFocus();
                }

            }
        });
        number8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(number8.getText().toString().isEmpty()) {
                    number7.setFocusableInTouchMode(true);
                    number7.requestFocus();
                }

            }
        });



        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_msg.setText("");
                String n1 = number1.getText().toString();
                String n2 = number2.getText().toString();
                String n3 = number3.getText().toString();
                String n4 = number4.getText().toString();
                String n5 = number5.getText().toString();
                String n6 = number6.getText().toString();
                String n7 = number7.getText().toString();
                String n8 = number8.getText().toString();
                if(!n1.isEmpty() && !n2.isEmpty() && !n3.isEmpty() && !n4.isEmpty() && !n5.isEmpty() && !n6.isEmpty() && !n7.isEmpty() && !n8.isEmpty() ){
                     String passcode1 = n1+n2+n3+n4;
                     String passcode2 = n5+n6+n7+n8;

                     if(passcode1.compareTo(passcode2)==0){

                         Intent intent = new Intent(register_passcode.this,formdashboard.class);
                         intent.putExtra("User_number",phone_number);
                         intent.putExtra("Passcode",passcode1);
                         startActivity(intent);
                         finish();
                     }
                     else{
                         error_msg.setText("Passcode is not match");
                     }
                }
                else{
                    error_msg.setText("All the fields are required");
                }
            }
        });

        set_passcode.setOnClickListener(new View.OnClickListener() {
            int count1=0;
            @Override
            public void onClick(View view) {
                if(count1==0){
                    set_passcode.setImageResource(R.drawable.ic_eye_slash_solid);
                    number1.setTransformationMethod(null);
                    number2.setTransformationMethod(null);
                    number3.setTransformationMethod(null);
                    number4.setTransformationMethod(null);
                    count1=1;

                }
                else{
                    set_passcode.setImageResource(R.drawable.ic_eye_solid);
                    number1.setTransformationMethod(new PasswordTransformationMethod());
                    number2.setTransformationMethod(new PasswordTransformationMethod());
                    number3.setTransformationMethod(new PasswordTransformationMethod());
                    number4.setTransformationMethod(new PasswordTransformationMethod());
                    count1=0;
                }
            }
        });

        re_passcode.setOnClickListener(new View.OnClickListener() {
            int count2=0;
            @Override
            public void onClick(View view) {
                if(count2==0){
                    re_passcode.setImageResource(R.drawable.ic_eye_slash_solid);
                    number5.setTransformationMethod(null);
                    number6.setTransformationMethod(null);
                    number7.setTransformationMethod(null);
                    number8.setTransformationMethod(null);
                    count2=1;

                }
                else{
                    re_passcode.setImageResource(R.drawable.ic_eye_solid);
                    number5.setTransformationMethod(new PasswordTransformationMethod());
                    number6.setTransformationMethod(new PasswordTransformationMethod());
                    number7.setTransformationMethod(new PasswordTransformationMethod());
                    number8.setTransformationMethod(new PasswordTransformationMethod());
                    count2=0;
                }
            }
        });
    }
}