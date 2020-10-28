package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    Context context;
    String user_id;
    ArrayList transaction_name,transaction_phone_number,transaction_amount,transaction_time,transaction_sender_id,transaction_image,transaction_id;
    int lastPosition = -1;

    public TransactionAdapter(Context context,String user_id, ArrayList transaction_sender_id,ArrayList transaction_name, ArrayList transaction_phone_number, ArrayList transaction_amount, ArrayList transaction_time,ArrayList transaction_image,ArrayList transaction_id) {
        this.context = context;
        this.user_id = user_id;
        this.transaction_sender_id = transaction_sender_id;
        this.transaction_name = transaction_name;
        this.transaction_phone_number = transaction_phone_number;
        this.transaction_amount = transaction_amount;
        this.transaction_time = transaction_time;
        this.transaction_image = transaction_image;
        this.transaction_id = transaction_id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.transction1_text.setTag(String.valueOf(transaction_id.get(position)));
        holder.transaction_name_text.setText(String.valueOf(transaction_name.get(position)));
        holder.transaction_phone_number_text.setText("+91-"+String.valueOf(transaction_phone_number.get(position)).substring(2));
        if(String.valueOf((transaction_sender_id.get(position))).compareTo(user_id)==0){
            holder.transaction_amount_text.setText("- "+String.valueOf(transaction_amount.get(position)));
            holder.transaction_amount_text.setTextColor(context.getResources().getColor(R.color.warning));
            holder.transactionamountsymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.debit_rs_symbol));
        }else{
            holder.transaction_amount_text.setText("+ "+String.valueOf(transaction_amount.get(position)));
            holder.transaction_amount_text.setTextColor(context.getResources().getColor(R.color.sucess));
            holder.transactionamountsymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.credit_rs_symbol));
        }
        holder.transaction_time_text.setText(String.valueOf(transaction_time.get(position)));
        holder.customer_image.setImageBitmap((Bitmap) transaction_image.get(position));
        setAnimation(holder.itemView,position);
    }

    private void setAnimation(View itemView, int position) {
        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return transaction_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView transaction_name_text,transaction_phone_number_text,transaction_amount_text,transaction_time_text;
        ImageView transactionamountsymbol,customer_image;
        MaterialCardView transction1_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transction1_text = itemView.findViewById(R.id.transction1);
            transaction_name_text = itemView.findViewById(R.id.customername);
            transaction_phone_number_text = itemView.findViewById(R.id.customerphonenumber);
            transaction_amount_text = itemView.findViewById(R.id.transactionamount);
            transaction_time_text = itemView.findViewById(R.id.transactiondate);
            transactionamountsymbol = itemView.findViewById(R.id.transactionamountsymbol);
            customer_image = itemView.findViewById(R.id.customer_image);
        }
    }
}
