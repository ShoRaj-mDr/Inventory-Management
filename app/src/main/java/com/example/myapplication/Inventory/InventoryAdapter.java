package com.example.myapplication.Inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.example.myapplication.R;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

//    private List<Item> items;   // ListItemssQuery.Item
    private final List<ListItemssQuery.Item> items;

    public InventoryAdapter(List<ListItemssQuery.Item> items) {
        this.items = items;
    }

    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_manage_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryAdapter.ViewHolder holder, int position) {
        ListItemssQuery.Item item = items.get(position);
        holder.itemName.setText(item.name());
        holder.itemQuantity.setText(String.valueOf(item.quantity()));
        holder.itemPrice.setText("$ " + item.price());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView itemName;
        private final TextView itemQuantity;
        private final TextView itemPrice;

        public ViewHolder(@NonNull View itemView) {
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
