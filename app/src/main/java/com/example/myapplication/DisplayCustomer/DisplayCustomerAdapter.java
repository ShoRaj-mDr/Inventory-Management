package com.example.myapplication.DisplayCustomer;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListCustomerssQuery;
import com.example.myapplication.R;

import java.util.List;

public class DisplayCustomerAdapter extends RecyclerView.Adapter<DisplayCustomerAdapter.ViewHolder> {

    private final List<ListCustomerssQuery.Item> customers;

    public DisplayCustomerAdapter(List<ListCustomerssQuery.Item> list) {
        this.customers = list;
    }

    @NonNull
    @Override
    public DisplayCustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_display_customer, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DisplayCustomerAdapter.ViewHolder holder, int position) {
        ListCustomerssQuery.Item customer = customers.get(position);
        String customerinfo = customer.name() + " (" + customer.email() + ")";
        holder.customerNum.setText(Integer.toString(position + 1) + ".");
        holder.customerName.setText(customerinfo);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView customerNum;
        private final TextView customerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNum = itemView.findViewById(R.id.customer_number);
            customerName = itemView.findViewById(R.id.customer_name);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Customer Name: " + this.customerName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
