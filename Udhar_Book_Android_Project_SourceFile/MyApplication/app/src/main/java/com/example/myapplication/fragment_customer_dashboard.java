package com.example.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class fragment_customer_dashboard extends Fragment {

    RelativeLayout add_contact;
    ImageView imageView;
    TextView save_contact, error_msg_contact_number, error_msg_contact_name;
    String contact_number_text, contact_name_text, user_id,user_phone_number, database_name = "", database_id;
    EditText contact_number, contact_name;
    TextInputLayout contact_name_layout;
    ArrayList<String> friend_id,transaction_name, transaction_phone_number;
    ArrayList<Integer> transaction_amount;
    ArrayList<Bitmap> transaction_image;
    ContactAdapter contactAdapter;
    RecyclerView transactionrecyclerview;
    Cursor cursor,cursor1;
    RelativeLayout relative_layout;
    ConstraintLayout constraintlayout;
    CodeScanner codeScanner;
    CodeScannerView scannView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_customer_dashboard, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");
        user_phone_number = sharedPreferences.getString("user_phone_number", "");

        add_contact = root.findViewById(R.id.add_contact);
        transactionrecyclerview = root.findViewById(R.id.transactionrecyclerview);
        relative_layout = root.findViewById(R.id.relative_layout);
        constraintlayout = root.findViewById(R.id.constraintlayout);
        scannView = root.findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(getContext(),scannView);

        friend_id = new ArrayList<>();
        transaction_name = new ArrayList<>();
        transaction_phone_number = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_image = new ArrayList<>();

        final DatabaseHelper myDB = new DatabaseHelper(getContext());

        cursor = myDB.customer_list_credit_transaction(user_id);
        cursor1 = myDB.customer_list_debit_transaction(user_id);
        if (cursor == null) {
            Toast.makeText(getContext(), "No Data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext() && cursor1.moveToNext()) {
                int credit_amount =Integer.parseInt(cursor.getString(4));
                int debit_amount =Integer.parseInt(cursor1.getString(4));
                friend_id.add(cursor.getString(0));
                transaction_name.add(cursor.getString(1));
                transaction_phone_number.add(cursor.getString(2));
                transaction_image.add(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0 , cursor.getBlob(3).length));
                transaction_amount.add((credit_amount - debit_amount));
            }
        }

        contactAdapter = new ContactAdapter(getContext(),friend_id,transaction_name,transaction_phone_number,transaction_amount,transaction_image);
        transactionrecyclerview.setAdapter(contactAdapter);
        transactionrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_contact_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                imageView = bottomSheetDialog.findViewById(R.id.imageView);
                contact_number = bottomSheetDialog.findViewById(R.id.contact_number);
                save_contact = bottomSheetDialog.findViewById(R.id.save_contact);
                contact_name_layout = bottomSheetDialog.findViewById(R.id.contact_name_layout);
                contact_name = bottomSheetDialog.findViewById(R.id.contact_name);
                error_msg_contact_number = bottomSheetDialog.findViewById(R.id.error_msg_contact_number);
                error_msg_contact_name = bottomSheetDialog.findViewById(R.id.error_msg_contact_name);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int[] temp_count = {1};

                        Dexter.withContext(getContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                codeScanner.startPreview();
                                temp_count[0] = 0;
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(getContext(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
                        if(temp_count[0] == 0){
                            relative_layout.setVisibility(View.GONE);
                            constraintlayout.setVisibility(View.VISIBLE);
                            bottomSheetDialog.hide();
                            Toast.makeText(getContext(), "Start Scaning", Toast.LENGTH_SHORT).show();
                            codeScanner.setDecodeCallback(new DecodeCallback() {
                                @Override
                                public void onDecoded(@NonNull final Result result) {


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            String[] parts = result.getText().split(Pattern.quote("\n"));
                                            String[] name = parts[0].split(Pattern.quote(":-"));
                                            String[] phone_number = parts[3].split(Pattern.quote(":-"));

                                            String customer_name = name[1].trim();
                                            String customer_phonenumber = (phone_number[1].trim()).substring(2);
                                            contact_name_layout.setVisibility(View.VISIBLE);
                                            error_msg_contact_name.setVisibility(View.VISIBLE);
                                            contact_name.setText(customer_name);
                                            contact_number.setText(customer_phonenumber);
                                            relative_layout.setVisibility(View.VISIBLE);
                                            constraintlayout.setVisibility(View.GONE);
                                            bottomSheetDialog.show();

                                        }
                                    });

                                }

                            });

                            scannView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    codeScanner.startPreview();
                                }
                            });
                        }

                    }
                });

                save_contact.setOnClickListener(new View.OnClickListener() {
                    int count = 0;

                    @Override
                    public void onClick(View view) {
                        contact_number_text = contact_number.getText().toString();
                        if (contact_number_text.length() == 10 && !contact_number_text.isEmpty() && contact_number_text.matches("^[0-9]{10}$")) {
                            contact_number_text = "91" + contact_number_text;

                            if(user_phone_number.compareTo(contact_number_text) != 0){
                                Cursor cursor2=myDB.check_friend_exist(user_id,contact_number_text);
                                if(cursor2.getCount()==0){
                                    if (count == 0) {
                                        Cursor cursor = myDB.check_usernumber_exist(contact_number_text, 1);
                                        if (cursor.getCount() != 0) {
                                            while (cursor.moveToNext()) {
                                                database_id = cursor.getString(0);
                                                database_name = cursor.getString(2);
                                            }
                                            contact_name.setText(database_name);
                                        }
                                    }

                                    error_msg_contact_number.setText("");
                                    contact_name_layout.setVisibility(View.VISIBLE);
                                    error_msg_contact_name.setVisibility(View.VISIBLE);

                                    contact_name_text = contact_name.getText().toString();

                                    if (!contact_name_text.isEmpty() && count == 1 && contact_name_text.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")) {
                                        error_msg_contact_number.setText("");
                                        if (database_name.compareTo(contact_name_text) == 0) {
                                            if (myDB.storeNewExistingFriendUserData(Integer.parseInt(user_id), Integer.parseInt(database_id))) {
                                                Toast.makeText(getContext(), "New Contact Added", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Resources res = getResources();
                                            String uri = "@drawable/"+contact_name_text.substring(0,1).toLowerCase();
                                            int imageResource = getResources().getIdentifier(uri,null,getContext().getPackageName());
                                            Drawable drawable = res.getDrawable(imageResource);
                                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                            byte[] bitMapData = stream.toByteArray();
                                            if (myDB.storeNewFriendUserData(user_id, contact_number_text, contact_name_text,bitMapData)) {
                                                Toast.makeText(getContext(), "New Contact Added", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        getFragmentManager().beginTransaction().replace(R.id.frame_container,new fragment_customer_dashboard()).commit();
                                        bottomSheetDialog.hide();

                                    } else {
                                        if (count == 1) {
                                            if (contact_name_text.isEmpty()) {
                                                error_msg_contact_name.setText("Contact Name is required");
                                            }else if(!contact_name_text.matches("^[a-zA-Z]{1}[a-zA-Z ]*$")){
                                                error_msg_contact_name.setText("Require Character and Whitespace Only");
                                            }
                                        }

                                    }
                                    count = 1;
                                }
                                else{
                                    error_msg_contact_number.setText("Already added in your contact list");
                                }
                            }
                            else{
                                error_msg_contact_number.setText("This number can't added.");
                            }

                        }
                        else {
                            if (contact_number_text.isEmpty()) {
                                error_msg_contact_number.setText("Contact Number is required");
                            } else if (contact_number_text.length() != 10) {
                                error_msg_contact_number.setText("Contact Number is not valid");
                            }else if(!contact_number_text.matches("^[0-9]{10}$")){
                                error_msg_contact_number.setText("Require only 10 digit");
                            }
                        }
                    }
                });

                bottomSheetDialog.show();

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
