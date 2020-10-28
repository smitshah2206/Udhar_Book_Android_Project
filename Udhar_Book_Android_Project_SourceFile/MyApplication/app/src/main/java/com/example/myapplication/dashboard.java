package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    DrawerLayout drawerLayout;
    NavigationView side_navigation;
    ImageView sidebar_icon,customer_image;
    TextView status_bar_text,customer_business_name,customer_name,transaction_date;
    EditText contact_number;
    String user_id,user_business_name,user_name;
    MaterialCardView edit_button;
    byte[] user_image;

    public void transaction_chat(View view){
        MaterialCardView card = (MaterialCardView) view;
        String a = card.getTag().toString();
        Intent intent = new Intent(getApplicationContext(),transaction_chat.class);
        intent.putExtra("Friend_id",a);
        startActivity(intent);
    }
    public void transaction_summary(View view){
        String transaction_id,transaction_sender_id = null,transaction_receiver_id = null,transaction_amount_text = null,transaction_remarks_text = null,transaction_date_text = null,sender_id,customer_phone_number_alert_text = null;
        Bitmap customer_image_alert_text = null;
        final ImageView close_alert,transactionamountsymbol,customer_image_alert,share_icon;
        TextView transaction_amount,transaction_remarks,transaction_time,customer_phone_number_alert;
        final MaterialCardView alert_dialog;
        final LinearLayout share_layout;

        MaterialCardView card = (MaterialCardView) view;
        transaction_id = card.getTag().toString();
        DatabaseHelper myDB = new DatabaseHelper(this);
        Cursor cursor = myDB.get_transaction_details(transaction_id);

        while (cursor.moveToNext()){
            transaction_sender_id = cursor.getString(1);
            transaction_receiver_id = cursor.getString(2);
            transaction_amount_text = cursor.getString(3);
            transaction_remarks_text = cursor.getString(5);
            transaction_date_text = cursor.getString(7);
        }

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_transaction_alert);

        alert_dialog = dialog.findViewById(R.id.alert_dialog);
        close_alert = dialog.findViewById(R.id.close_alert);
        customer_image_alert = dialog.findViewById(R.id.customer_image);
        customer_phone_number_alert = dialog.findViewById(R.id.customer_contact_number);
        transactionamountsymbol = dialog.findViewById(R.id.transactionamountsymbol);
        transaction_amount = dialog.findViewById(R.id.transaction_amount);
        transaction_remarks = dialog.findViewById(R.id.transaction_remarks);
        transaction_time = dialog.findViewById(R.id.transaction_time);
        share_icon = dialog.findViewById(R.id.share_icon);
        share_layout = dialog.findViewById(R.id.share_layout);

        if(transaction_sender_id.compareTo(user_id)==0){
            sender_id = transaction_receiver_id;
            transaction_amount.setText("- "+transaction_amount_text);
            transaction_amount.setTextColor(getApplicationContext().getResources().getColor(R.color.warning));
            transactionamountsymbol.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.debit_rs_symbol));
            close_alert.setImageResource(R.drawable.alertbox_cross_icon_debit);
        }
        else{
            sender_id = transaction_sender_id;
            transaction_amount.setText("+ "+transaction_amount_text);
            transaction_amount.setTextColor(getApplicationContext().getResources().getColor(R.color.sucess));
            transactionamountsymbol.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.credit_rs_symbol));
            close_alert.setImageResource(R.drawable.alertbox_cross_icon_credit);
            share_icon.setImageResource(R.drawable.credit_share_icon);
        }

        Cursor cursor1 = myDB.get_user_details(sender_id);

        while (cursor1.moveToNext()){
            customer_phone_number_alert_text = "+91-"+cursor1.getString(0).substring(2);
            customer_image_alert_text = BitmapFactory.decodeByteArray(cursor1.getBlob(4), 0 , cursor1.getBlob(4).length);
        }

        transaction_remarks.setText(transaction_remarks_text);
        transaction_time.setText(transaction_date_text);
        customer_phone_number_alert.setText(customer_phone_number_alert_text);
        customer_image_alert.setImageBitmap(customer_image_alert_text);

        close_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                share_layout.setVisibility(View.INVISIBLE);
                close_alert.setVisibility(View.INVISIBLE);
                Bitmap bitmap = Bitmap.createBitmap(alert_dialog.getWidth(),alert_dialog.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                alert_dialog.draw(canvas);

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + user_name +"_"+user_business_name+".jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");

        final DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
        Cursor cursor = myDB.get_user_details(user_id);
        while (cursor.moveToNext()){
            user_name = cursor.getString(1);
            user_business_name = cursor.getString(2);
            user_image = cursor.getBlob(4);
        }

        final Bitmap bitmap = BitmapFactory.decodeByteArray(user_image, 0 , user_image.length);

        /* Side Navigation */

        drawerLayout = findViewById(R.id.side_drawer);
        side_navigation = findViewById(R.id.side_navigation);
        sidebar_icon = findViewById(R.id.sidebar_icon);
        navigationDrawer();


        View hView =  side_navigation.getHeaderView(0);
        customer_image = hView.findViewById(R.id.customer_image);
        customer_business_name = hView.findViewById(R.id.customer_business_name);
        customer_name = hView.findViewById(R.id.customer_name);
        edit_button = hView.findViewById(R.id.edit_button);

        customer_image.setImageBitmap(bitmap);
        customer_business_name.setText(user_business_name);
        customer_name.setText(user_name);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = new edit_formdashboard();
                status_bar_text.setText(R.string.editformdashboardheading);
                drawerLayout.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
            }
        });

        /* Status Bar */
        status_bar_text = findViewById(R.id.status_bar_text);

        /* Bottom Navigation Fragment */

        final BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigation);
        bottom_nav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new fragment_home_dashboard()).commit();

    }

    /* Side Navigation */
    private void navigationDrawer() {
        side_navigation.bringToFront();
        side_navigation.setNavigationItemSelectedListener(this);
        side_navigation.setCheckedItem(R.id.nav_share);
        sidebar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_qrcode:
                selectedFragment = new fragment_qrcode();
                status_bar_text.setText(R.string.qrdashboardstatusbar);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                break;
            case R.id.nav_help:
                selectedFragment = new contact_us();
                status_bar_text.setText("Help & Support");
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                break;
            case R.id.nav_share:
                String message = "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.nav_logout:
                SharedPreferences SharedPreferences = getSharedPreferences("UserDetails",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = SharedPreferences.edit();
                myEdit.putBoolean("is_logged_in",false);
                myEdit.commit();
                Intent intent=new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /* Bottom Navigation Fragment */

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new fragment_home_dashboard();
                    status_bar_text.setText(R.string.homedashboardstatusbar);
                    break;
                case R.id.nav_user:
                    selectedFragment = new fragment_customer_dashboard();
                    status_bar_text.setText(R.string.selectcustomerdashboardstatusbar);
                    break;
                case R.id.nav_book:
                    selectedFragment = new fragment_transaction_dashboard();
                    status_bar_text.setText(R.string.transactionlistdashboardstatusbar);
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            EditText user_location = findViewById(R.id.user_location);
            user_location.setText(place.getName());
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String month_name = new DateFormatSymbols().getMonths()[month];
        String temp_date = String.valueOf(day) + " - " + month_name.substring(0, 3) + " - " + String.valueOf(year);
        transaction_date.setText(temp_date);

    }
}