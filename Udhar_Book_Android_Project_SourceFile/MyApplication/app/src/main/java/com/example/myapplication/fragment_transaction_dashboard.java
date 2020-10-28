package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class fragment_transaction_dashboard extends Fragment implements AdapterView.OnItemSelectedListener {

    String user_id;
    Spinner spinner;
    Cursor cursor;
    ArrayList<String> transaction_name,transaction_phone_number,transaction_time,transaction_amount,transaction_sender_id,transaction_id;
    ArrayList<Bitmap> transaction_image;
    TransactionAdapter transactionAdapter;
    RecyclerView transactionrecyclerview;
    DatabaseHelper myDB;
    private Date oneWayTripDate;
    ImageView download_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_transaction_dashboard,container,false);
        spinner = root.findViewById(R.id.spinner1);
        transactionrecyclerview = root.findViewById(R.id.transactionrecyclerview);
        download_button = root.findViewById(R.id.download_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),R.array.numbers,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature Working Progress", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        myDB = new DatabaseHelper(getContext());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");

        transaction_name = new ArrayList<>();
        transaction_phone_number = new ArrayList<>();
        transaction_sender_id = new ArrayList<>();
        transaction_time = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_image = new ArrayList<>();
        transaction_id = new ArrayList<>();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        if(i==0) {
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            cursor = myDB.all_transaction(user_id);
        }
        else if(i==1){
            progressDialog.show();
            cursor = myDB.credit_transaction(user_id);
        }
        else if(i==2){
            progressDialog.show();
            cursor = myDB.debit_transaction(user_id);
        }
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
        if (cursor == null) {
            Toast.makeText(getContext(), "No Data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                transaction_phone_number.add(cursor.getString(1));
                transaction_name.add(cursor.getString(2));
                if(cursor.getBlob(3)==null){
                    transaction_image.add(null);
                }
                else{
                    transaction_image.add(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0 , cursor.getBlob(3).length));
                }
                transaction_sender_id.add(cursor.getString(5));
                transaction_amount.add(cursor.getString(7));
                String date = cursor.getString(8);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                try {
                    oneWayTripDate = input.parse(date);  // parse input
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transaction_time.add(output.format(oneWayTripDate));
                transaction_id.add(cursor.getString(9));
            }
        }
        transactionAdapter = new TransactionAdapter(getContext(),user_id,transaction_sender_id,transaction_name,transaction_phone_number,transaction_amount,transaction_time,transaction_image,transaction_id);
        transactionrecyclerview.setAdapter(transactionAdapter);
        transactionrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog.dismiss();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
