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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    ArrayList friend_id,transaction_name,transaction_phone_number,transaction_amount,transaction_image;
    int lastPosition = -1;

    public ContactAdapter(Context context,ArrayList friend_id, ArrayList transaction_name, ArrayList transaction_phone_number, ArrayList transaction_amount,ArrayList transaction_image) {
        this.context = context;
        this.friend_id = friend_id;
        this.transaction_name = transaction_name;
        this.transaction_phone_number = transaction_phone_number;
        this.transaction_amount = transaction_amount;
        this.transaction_image = transaction_image;
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_row,parent,false);
        return new ContactAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {

        holder.friend_id_text.setTag(String.valueOf(friend_id.get(position)));
        holder.transaction_name_text.setText(String.valueOf(transaction_name.get(position)));
        holder.customer_image.setImageBitmap((Bitmap) transaction_image.get(position));
        holder.transaction_phone_number_text.setText("+91-"+String.valueOf(transaction_phone_number.get(position)).substring(2));
        if((Integer) transaction_amount.get(position) >= 0){
            holder.transaction_amount_text.setText("+ "+String.valueOf(transaction_amount.get(position)));
            holder.transaction_amount_text.setTextColor(context.getResources().getColor(R.color.sucess));
            holder.transactionamountsymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.credit_rs_symbol));
        }else{
            holder.transaction_amount_text.setText("- "+String.valueOf(Math.abs((Integer) transaction_amount.get(position))));
            holder.transaction_amount_text.setTextColor(context.getResources().getColor(R.color.warning));
            holder.transactionamountsymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.debit_rs_symbol));
        }
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

        TextView transaction_name_text,transaction_phone_number_text,transaction_amount_text;
        MaterialCardView friend_id_text;
        ImageView transactionamountsymbol,customer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            friend_id_text = itemView.findViewById(R.id.transction);
            transaction_name_text = itemView.findViewById(R.id.customername);
            transaction_phone_number_text = itemView.findViewById(R.id.customerphonenumber);
            transaction_amount_text = itemView.findViewById(R.id.transactionamount);
            transactionamountsymbol = itemView.findViewById(R.id.transactionamountsymbol);
            customer_image = itemView.findViewById(R.id.customer_image);
        }
    }

}
