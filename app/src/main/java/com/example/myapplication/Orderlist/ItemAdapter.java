package com.example.myapplication.Orderlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Item.Item;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The adapter class for the item RecyclerView, contains the item
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<Item> items;

    public ItemAdapter(List<Item> itemList) {
        this.items = itemList;
    }


    @NotNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
            inflate(R.layout.list_manage_inventory, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
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


    public void deleteItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemName = itemView.findViewById(R.id.manageInventory_itemName);
            itemQuantity = itemView.findViewById(R.id.manageInventory_itemQuantity);
            itemPrice = itemView.findViewById(R.id.manageInventory_itemPrice);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Item: " + this.itemName.getText(), Toast.LENGTH_SHORT).show();
        }
    }

}