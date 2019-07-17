package com.example.trey.registertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReceiptListAdapter extends ArrayAdapter<Receipt> {

    private static final String TAG = "ReceiptListAdapter";

    private Context mcontext;
    int mResource;

    public ReceiptListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Receipt> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String storeName = getItem(position).getStoreName();
        String price = getItem(position).getPrice();
        String category = getItem(position).getCategory();
        String date = getItem(position).getDate();
        String receiptID = getItem(position).getReceiptID();

        Receipt receipt = new Receipt(storeName, price, category, date, receiptID);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvStoreName = convertView.findViewById(R.id.textView1);
        TextView tvPrice = convertView.findViewById(R.id.textView2);
        TextView tvCategory = convertView.findViewById(R.id.textView3);
        TextView tvDate = convertView.findViewById(R.id.textView4);

        tvStoreName.setText(storeName);
        tvPrice.setText(price);
        tvCategory.setText(category);
        tvDate.setText(date);

        return convertView;
    }
}