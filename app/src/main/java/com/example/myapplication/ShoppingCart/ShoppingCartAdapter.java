package com.example.myapplication.ShoppingCart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.example.myapplication.Item.Item;
import com.example.myapplication.Orderlist.ItemAdapter;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private final List<Item> items;

    public ShoppingCartAdapter(List<Item> itemList) {
        this.items = itemList;
    }


    @NotNull
    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_shopping_cart, parent, false);
        return new ShoppingCartAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ShoppingCartAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
//        if (item.getName() != null) {
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.itemPrice.setText("$ " + item.getPrice());
//        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemName = itemView.findViewById(R.id.itemView);
            itemQuantity = itemView.findViewById(R.id.quantityView);
            itemPrice = itemView.findViewById(R.id.priceView);
        }

        @Override
        public void onClick(View view) {
            // Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.itemName.getText(), Toast.LENGTH_SHORT).show();
            Toast.makeText(view.getContext(), "Item: " + this.itemName.getText(), Toast.LENGTH_SHORT).show();
        }
    }

}