package com.example.myapplication.Checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Item.Item;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ConfirmListAdapter extends ArrayAdapter<Item> {

    private static final String TAG = "ConfirmListAdapter";

    private Context mContext;
    int mResource;

    public ConfirmListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Item> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get item info
        String name = getItem(position).getName();
        int quantity = getItem(position).getQuantity();
        double price = getItem(position).getPrice();
        int maxQuant = getItem(position).getMaxQuantity();

        Item item = new Item(name, price, quantity, maxQuant);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView itemName = (TextView) convertView.findViewById(R.id.itemView);
        TextView itemQuantity = (TextView) convertView.findViewById(R.id.quantityView);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.priceView);

        itemName.setText(name);
        itemQuantity.setText(String.valueOf(quantity));
        itemPrice.setText("$" + Double.toString(price));

        final String temp = "Name = " + name + " Price = $ " + price + " Quantity = " + quantity;

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, temp, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}