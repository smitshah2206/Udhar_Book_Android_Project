package com.example.myapplication;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    Context context;
    String user_id;
    ArrayList transaction_amount,transaction_time,transaction_remarks,transaction_sender_id,transaction_id;
    int lastPosition = -1;

    public ChatAdapter(Context context,String user_id,ArrayList transaction_sender_id, ArrayList transaction_amount,ArrayList transaction_remarks, ArrayList transaction_time, ArrayList transaction_id) {
        this.context = context;
        this.user_id = user_id;
        this.transaction_sender_id = transaction_sender_id;
        this.transaction_amount = transaction_amount;
        this.transaction_remarks = transaction_remarks;
        this.transaction_time = transaction_time;
        this.transaction_id = transaction_id;
    }

    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transaction_chat_row,parent,false);
        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {

        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.bottomMargin = dp1 * 10;
        params.topMargin = dp1 * 10;
        params.rightMargin = dp1 * 15;

        if (String.valueOf(transaction_sender_id.get(position)).compareTo(user_id) == 0){
            holder.transaction_amount_text.setText("- "+String.valueOf(transaction_amount.get(position)));
            holder.transaction_amount_text.setTextColor(context.getResources().getColor(R.color.warning));
            holder.transactionamountsymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.debit_rs_symbol));
            holder.share_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.debit_share_icon));
        }else{
            holder.transaction_amount_text.setText("+ "+String.valueOf(transaction_amount.get(position)));
            holder.transaction_amount_text.setTextColor(context.getResources().getColor(R.color.sucess));
            holder.transction.setLayoutParams(params);
            holder.transactionamountsymbol.setImageDrawable(context.getResources().getDrawable(R.drawable.credit_rs_symbol));
            holder.share_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.credit_share_icon));
        }
        holder.share_layout.setTag(String.valueOf(transaction_id.get(position)));
        holder.transaction_remarks_text.setText(String.valueOf(transaction_remarks.get(position)));
        holder.transaction_time_text.setText(String.valueOf(transaction_time.get(position)));
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
        return transaction_amount.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView transaction_amount_text,transaction_time_text,transaction_remarks_text;
        MaterialCardView transction;
        ImageView transactionamountsymbol,share_icon;
        LinearLayout share_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transction = itemView.findViewById(R.id.transction);
            transaction_amount_text = itemView.findViewById(R.id.transaction_amount);
            transaction_remarks_text = itemView.findViewById(R.id.transaction_remarks);
            transaction_time_text = itemView.findViewById(R.id.transaction_time);
            transactionamountsymbol = itemView.findViewById(R.id.transactionamountsymbol);
            share_icon = itemView.findViewById(R.id.share_icon);
            share_layout = itemView.findViewById(R.id.share_layout);
        }
    }
}
