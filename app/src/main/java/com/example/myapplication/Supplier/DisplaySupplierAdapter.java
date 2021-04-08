package com.example.myapplication.Supplier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListSupplierssQuery;
import com.example.myapplication.R;

import java.util.List;


public class DisplaySupplierAdapter extends RecyclerView.Adapter<DisplaySupplierAdapter.ViewHolder>{

    private final List<ListSupplierssQuery.Item> suppliers;

    public DisplaySupplierAdapter(List<ListSupplierssQuery.Item> s) {
        this.suppliers = s;
    }

    // Note: Re-using list_display_customer. Design a supplier layout if needed!
    @NonNull
    @Override
    public DisplaySupplierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_display_customer, parent, false);
        return new DisplaySupplierAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplaySupplierAdapter.ViewHolder holder, int position) {
        ListSupplierssQuery.Item item = suppliers.get(position);
        holder.supplierNumber.setText(Integer.toString(position + 1));
        holder.supplierName.setText(item.name());
    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView supplierNumber;
        private final TextView supplierName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            supplierNumber = itemView.findViewById(R.id.customer_number);
            supplierName = itemView.findViewById(R.id.customer_name);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Item: " + this.supplierName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
