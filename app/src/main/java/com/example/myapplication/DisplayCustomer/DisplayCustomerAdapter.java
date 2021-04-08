package com.example.myapplication.DisplayCustomer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class DisplayCustomerAdapter extends RecyclerView.Adapter<DisplayCustomerAdapter.ViewHolder> {

    private final List<String> customers;

    public DisplayCustomerAdapter(List<String> list) {
        this.customers = list;
    }

    @NonNull
    @Override
    public DisplayCustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_display_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayCustomerAdapter.ViewHolder holder, int position) {
        String str = customers.get(position);
        holder.customerNum.setText(Integer.toString(position + 1));
        holder.customerName.setText(str);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView customerNum;
        private final TextView customerName;
//        private final TextView customerAmount;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNum = itemView.findViewById(R.id.customer_number);
            customerName = itemView.findViewById(R.id.customer_name);
//            customerAmount = null;
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Customer Name: " + this.customerName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
