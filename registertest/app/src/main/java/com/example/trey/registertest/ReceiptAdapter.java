package com.example.trey.registertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.example.trey.registertest.HomeActivity.receiptArrayList;


public class ReceiptAdapter extends BaseAdapter{

    public static String globalPreferenceName = "com.trey.profile";
    Context mContext;
    LayoutInflater inflater;
    List<Receipt> receipts;
    ArrayList<Receipt> receiptArrayList;

    //constructor
    public ReceiptAdapter(Context context, List<Receipt> receiptList) {
        mContext = context;
        this.receipts = receiptList;
        inflater = LayoutInflater.from(mContext);
        this.receiptArrayList = new ArrayList<Receipt>();
        this.receiptArrayList.addAll(receipts);
    }

    public class ViewHolder{
        TextView storeName, price, date, category;
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Object getItem(int i) {
        return receipts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_view_layout, null);

            //locate the views in row.xml
            holder.storeName = view.findViewById(R.id.textView1);
            holder.price = view.findViewById(R.id.textView2);
            holder.date = view.findViewById(R.id.textView3);
            holder.category = view.findViewById(R.id.textView4);

            view.setTag(holder);

        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the results into textviews
        holder.storeName.setText(receipts.get(position).getStoreName());
        holder.price.setText(receipts.get(position).getPrice());
        holder.date.setText(receipts.get(position).getDate());
        holder.category.setText(receipts.get(position).getCategory());


        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (!receipts.get(position).getStoreName().isEmpty()){
                    Intent intent = new Intent(mContext, OneReceipt.class);
                    String receiptID = receipts.get(position).getReceiptID();
                    SharedPreferences settings = mContext.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                    settings.edit().remove("receiptID").apply();
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(globalPreferenceName, MODE_PRIVATE).edit();
                    editor.putString("receiptID", receiptID);
                    editor.apply();
                    //intent.putExtra("actionBarTitle", "Battery");
                    //intent.putExtra("contentTv", "This is Battery detail...");
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        receipts.clear();
        if (charText.length()==0){
            receipts.addAll(receiptArrayList);
        }
        else {
            for (Receipt receipt : receiptArrayList){
                if (receipt.getStoreName().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    receipts.add(receipt);
                }
            }
        }
        notifyDataSetChanged();
    }

}