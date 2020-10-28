package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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


public class fragment_home_dashboard extends Fragment {
    String user_id;
    TextView debitbalance,creditbalance,netbalance,see_all;
    ImageView net_amount_symbol,debit_amount_symbol,credit_amount_symbol;
    int debit_amount,credit_amount;
    Cursor cursor;
    ArrayList<String> transaction_name,transaction_phone_number,transaction_time,transaction_amount,transaction_sender_id,transaction_id;
    ArrayList<Bitmap> transaction_image;
    TransactionAdapter transactionAdapter;
    RecyclerView transactionrecyclerview;
    private Date oneWayTripDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_dashboard, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");
        netbalance = root.findViewById(R.id.netbalance);
        net_amount_symbol = root.findViewById(R.id.net_amount_symbol);
        debitbalance = root.findViewById(R.id.debitbalance);
        debit_amount_symbol = root.findViewById(R.id.debit_amount_symbol);
        creditbalance = root.findViewById(R.id.creditbalance);
        credit_amount_symbol = root.findViewById(R.id.credit_amount_symbol);
        transactionrecyclerview = root.findViewById(R.id.transactionrecyclerview);
        see_all = root.findViewById(R.id.see_all);

        transaction_name = new ArrayList<>();
        transaction_phone_number = new ArrayList<>();
        transaction_sender_id = new ArrayList<>();
        transaction_time = new ArrayList<>();
        transaction_amount = new ArrayList<>();
        transaction_image = new ArrayList<>();
        transaction_id = new ArrayList<>();

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new fragment_transaction_dashboard()).commit();
            }
        });

        DatabaseHelper myDB = new DatabaseHelper(getContext());

        debit_amount = myDB.debit_transaction_amount(user_id);
        debitbalance.setText("- "+debit_amount);
        debit_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.debit_rs_symbol));

        credit_amount = myDB.credit_transaction_amount(user_id);
        creditbalance.setText("+ "+credit_amount);
        credit_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.credit_rs_symbol));

        if((credit_amount - debit_amount) >=0 ){
            netbalance.setText("+ "+Math.abs(credit_amount - debit_amount));
            netbalance.setTextColor(getResources().getColor(R.color.sucess));
            net_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.credit_rs_symbol));
        }else{
            netbalance.setText("- "+Math.abs(credit_amount - debit_amount));
            netbalance.setTextColor(getResources().getColor(R.color.warning));
            net_amount_symbol.setImageDrawable(getResources().getDrawable(R.drawable.debit_rs_symbol));
        }

        cursor = myDB.all_transaction(user_id);
        if (cursor == null){
            Toast.makeText(getContext(), "No Data available", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                transaction_phone_number.add(cursor.getString(1));
                transaction_name.add(cursor.getString(2));
                transaction_image.add(BitmapFactory.decodeByteArray(cursor.getBlob(3), 0 , cursor.getBlob(3).length));
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

        return root;
    }

}
