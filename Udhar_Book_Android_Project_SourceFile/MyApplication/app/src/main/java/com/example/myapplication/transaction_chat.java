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
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class transaction_chat extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout share_layout;
    ImageView sidebar_icon;
    Intent intent;
    String Friend_id, user_id;
    ArrayList<String> transaction_amount, transaction_time, transaction_remarks, transaction_sender_id, transaction_id;
    Cursor cursor;
    RecyclerView transaction_chat_recycle;
    ChatAdapter chatAdapter;
    MaterialCardView transaction_debit, transaction_credit;
    TextView transaction_date, save_debit, transaction_name, transaction_balance, save_credit, friend_name, error_msg_transaction_amount, error_msg_transaction_date, error_msg_transaction_name;
    String transaction_date_text, transaction_name_text, transaction_balance_text, Friend_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_chat);
        getSupportActionBar().hide();

        sidebar_icon = findViewById(R.id.sidebar_icon);
        transaction_chat_recycle = findViewById(R.id.transaction_chat_recycle);
        transaction_debit = findViewById(R.id.transaction_debit);
        transaction_credit = findViewById(R.id.transaction_credit);
        friend_name = findViewById(R.id.friend_name);
        share_layout = findViewById(R.id.share_layout);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");
        intent = getIntent();
        Friend_id = intent.getStringExtra("Friend_id");

        DatabaseHelper myDB = new DatabaseHelper(this);
        Cursor cursor = myDB.get_user_details(Friend_id);
        while (cursor.moveToNext()) {
            Friend_name = cursor.getString(1);
        }

        friend_name.setText(Friend_name);

        transaction_sender_id = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_remarks = new ArrayList<>();
        transaction_time = new ArrayList<>();
        transaction_id = new ArrayList<>();

        transaction_debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(transaction_chat.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_debit_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                transaction_balance = bottomSheetDialog.findViewById(R.id.transaction_amount);
                transaction_name = bottomSheetDialog.findViewById(R.id.transaction_name);
                transaction_date = bottomSheetDialog.findViewById(R.id.transaction_date);
                error_msg_transaction_amount = bottomSheetDialog.findViewById(R.id.error_msg_transaction_amount);
                error_msg_transaction_date = bottomSheetDialog.findViewById(R.id.error_msg_transaction_date);
                error_msg_transaction_name = bottomSheetDialog.findViewById(R.id.error_msg_transaction_name);
                save_debit = bottomSheetDialog.findViewById(R.id.save_debit);

                transaction_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(transaction_chat.this, transaction_chat.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });

                save_debit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transaction_balance_text = transaction_balance.getText().toString();
                        transaction_name_text = transaction_name.getText().toString();
                        transaction_date_text = transaction_date.getText().toString();

                        int count1 = 1, count2 = 1, count3 = 1;

                        error_msg_transaction_name.setVisibility(View.GONE);
                        error_msg_transaction_amount.setVisibility(View.GONE);
                        error_msg_transaction_date.setVisibility(View.GONE);

                        if (transaction_name_text.isEmpty()) {
                            count1 = 0;
                            error_msg_transaction_name.setVisibility(View.VISIBLE);
                            error_msg_transaction_name.setText("This is required");
                        }
                        if (transaction_balance_text.isEmpty()) {
                            count2 = 0;
                            error_msg_transaction_amount.setVisibility(View.VISIBLE);
                            error_msg_transaction_amount.setText("This is required");
                        }
                        if (transaction_date_text.isEmpty()) {
                            count3 = 0;
                            error_msg_transaction_date.setVisibility(View.VISIBLE);
                            error_msg_transaction_date.setText("This is required");
                        }
                        if (count1 == 1 && count2 == 1 && count3 == 1) {
                            DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
                            String tost_message = null;
                            if (myDB.storeNewDebitTransaction(user_id, Friend_id, transaction_balance_text, transaction_name_text, transaction_date_text)) {
                                tost_message = "Transaction Added";
                            } else {
                                tost_message = "Something went wrong";
                            }
                            transaction_sender_id = new ArrayList<>();
                            transaction_amount = new ArrayList<>();
                            transaction_remarks = new ArrayList<>();
                            transaction_time = new ArrayList<>();
                            transaction_id = new ArrayList<>();
                            Fetch_Transaction(user_id, Friend_id);
                            chatAdapter = new ChatAdapter(getApplicationContext(), user_id, transaction_sender_id, transaction_amount, transaction_remarks, transaction_time, transaction_id);
                            transaction_chat_recycle.setAdapter(chatAdapter);
                            transaction_chat_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            transaction_chat_recycle.smoothScrollToPosition(transaction_amount.size());
                            Toast.makeText(transaction_chat.this, tost_message, Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.hide();
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });


        transaction_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(transaction_chat.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_credit_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                transaction_balance = bottomSheetDialog.findViewById(R.id.transaction_amount);
                transaction_name = bottomSheetDialog.findViewById(R.id.transaction_name);
                transaction_date = bottomSheetDialog.findViewById(R.id.transaction_date);
                error_msg_transaction_amount = bottomSheetDialog.findViewById(R.id.error_msg_transaction_amount);
                error_msg_transaction_date = bottomSheetDialog.findViewById(R.id.error_msg_transaction_date);
                error_msg_transaction_name = bottomSheetDialog.findViewById(R.id.error_msg_transaction_name);
                save_credit = bottomSheetDialog.findViewById(R.id.save_credit);

                transaction_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(transaction_chat.this, transaction_chat.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                });

                save_credit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        transaction_balance_text = transaction_balance.getText().toString();
                        transaction_name_text = transaction_name.getText().toString();
                        transaction_date_text = transaction_date.getText().toString();

                        int count1 = 1, count2 = 1, count3 = 1;

                        error_msg_transaction_name.setText("");
                        error_msg_transaction_amount.setText("");
                        error_msg_transaction_date.setText("");

                        if (transaction_name_text.isEmpty()) {
                            count1 = 0;
                            error_msg_transaction_name.setText("This is required");
                        }
                        if (transaction_balance_text.isEmpty()) {
                            count2 = 0;
                            error_msg_transaction_amount.setText("This is required");
                        }
                        if (transaction_date_text.isEmpty()) {
                            count3 = 0;
                            error_msg_transaction_date.setText("This is required");
                        }
                        if (count1 == 1 && count2 == 1 && count3 == 1) {
                            DatabaseHelper myDB = new DatabaseHelper(getApplicationContext());
                            String tost_message = null;
                            if (myDB.storeNewCreditTransaction(user_id, Friend_id, transaction_balance_text, transaction_name_text, transaction_date_text)) {
                                tost_message = "Transaction Added";
                            } else {
                                tost_message = "Something went wrong";
                            }
                            transaction_sender_id = new ArrayList<>();
                            transaction_amount = new ArrayList<>();
                            transaction_remarks = new ArrayList<>();
                            transaction_time = new ArrayList<>();
                            transaction_id = new ArrayList<>();
                            Fetch_Transaction(user_id, Friend_id);
                            chatAdapter = new ChatAdapter(getApplicationContext(), user_id, transaction_sender_id, transaction_amount, transaction_remarks, transaction_time, transaction_id);
                            transaction_chat_recycle.setAdapter(chatAdapter);
                            transaction_chat_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            transaction_chat_recycle.smoothScrollToPosition(transaction_amount.size());
                            Toast.makeText(transaction_chat.this, tost_message, Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.hide();
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });


        Fetch_Transaction(user_id, Friend_id);
        chatAdapter = new ChatAdapter(getApplicationContext(), user_id, transaction_sender_id, transaction_amount, transaction_remarks, transaction_time, transaction_id);
        transaction_chat_recycle.setAdapter(chatAdapter);
        transaction_chat_recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        transaction_chat_recycle.smoothScrollToPosition(transaction_amount.size());

    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    public void Fetch_Transaction(String userid, String friendid) {
        Date oneWayTripDate = null;
        DatabaseHelper myDB = new DatabaseHelper(this);
        cursor = myDB.user_friend_transaction(userid, friendid);
        while (cursor.moveToNext()) {
            transaction_sender_id.add(cursor.getString(0));
            transaction_amount.add(cursor.getString(1));
            transaction_remarks.add(cursor.getString(2));
            String date = cursor.getString(3);
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            try {
                oneWayTripDate = input.parse(date);  // parse input
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction_time.add(output.format(oneWayTripDate));
            transaction_id.add(cursor.getString(4));
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String month_name = new DateFormatSymbols().getMonths()[month];
        String temp_date = String.valueOf(day) + " - " + month_name.substring(0, 3) + " - " + String.valueOf(year);
        transaction_date.setText(temp_date);

    }

    public void share_transaction(View view) {
        String transaction_id;
        String transaction_sender_id = null;
        String transaction_receiver_id = null;
        String transaction_amount_text = null;
        String transaction_remarks_text = null;
        String transaction_date_text = null;
        String sender_id;
        String customer_phone_number_alert_text = null;
        final String[] user_name = new String[1];
        final String[] user_business_name = new String[1];
        Bitmap customer_image_alert_text = null;
        final ImageView close_alert, transactionamountsymbol, customer_image_alert, share_icon;
        TextView transaction_amount, transaction_remarks, transaction_time, customer_phone_number_alert;
        final MaterialCardView alert_dialog;
        final LinearLayout share_layout;

        LinearLayout card = (LinearLayout) view;
        transaction_id = card.getTag().toString();
        final DatabaseHelper myDB = new DatabaseHelper(this);
        Cursor cursor = myDB.get_transaction_details(transaction_id);

        while (cursor.moveToNext()) {
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

        if (transaction_sender_id.compareTo(user_id) == 0) {
            sender_id = transaction_receiver_id;
            transaction_amount.setText("- " + transaction_amount_text);
            transaction_amount.setTextColor(getApplicationContext().getResources().getColor(R.color.warning));
            transactionamountsymbol.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.debit_rs_symbol));
            close_alert.setImageResource(R.drawable.alertbox_cross_icon_debit);
        } else {
            sender_id = transaction_sender_id;
            transaction_amount.setText("+ " + transaction_amount_text);
            transaction_amount.setTextColor(getApplicationContext().getResources().getColor(R.color.sucess));
            transactionamountsymbol.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.credit_rs_symbol));
            close_alert.setImageResource(R.drawable.alertbox_cross_icon_credit);
            share_icon.setImageResource(R.drawable.credit_share_icon);
        }

        Cursor cursor1 = myDB.get_user_details(sender_id);

        while (cursor1.moveToNext()) {
            customer_phone_number_alert_text = "+91-" + cursor1.getString(0).substring(2);
            customer_image_alert_text = BitmapFactory.decodeByteArray(cursor1.getBlob(4), 0, cursor1.getBlob(4).length);
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
                Bitmap bitmap = Bitmap.createBitmap(alert_dialog.getWidth(), alert_dialog.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                alert_dialog.draw(canvas);
                Cursor cursor2 = myDB.get_user_details(user_id);
                while (cursor2.moveToNext()) {
                    user_name[0] = cursor2.getString(1);
                    user_business_name[0] = cursor2.getString(2);
                }

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + user_name[0] + "_" + user_business_name[0] + ".jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
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

}