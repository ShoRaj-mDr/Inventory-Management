package com.example.myapplication.DisplayCustomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.DeleteCustomersMutation;
import com.amazonaws.amplify.generated.graphql.ListCustomerssQuery;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;

import java.util.List;

import javax.annotation.Nonnull;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import type.DeleteCustomersInput;
import type.DeleteSuppliersInput;

public class DisplayCustomerAdapter extends RecyclerView.Adapter<DisplayCustomerAdapter.ViewHolder> {

    private final List<ListCustomerssQuery.Item> customers;
    private Context mContext;

    public DisplayCustomerAdapter(List<ListCustomerssQuery.Item> list, Context context) {
        this.customers = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DisplayCustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_display_customer, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final DisplayCustomerAdapter.ViewHolder holder, int position) {
        final ListCustomerssQuery.Item customer = customers.get(position);
        final String customerinfo = customer.name();
        holder.customerNum.setText((position + 1) + ".");
        holder.customerName.setText(customerinfo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "Customer Name: " + customerinfo, Toast.LENGTH_SHORT).show();
                displayDialogBox(customer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void displayDialogBox(final ListCustomerssQuery.Item customer) {
        MaterialDialog mDialog = new MaterialDialog.Builder((Activity) mContext)
                .setTitle(customer.name())
                .setMessage("Customer ID: " + customer.id() + '\n'
                        + "\nEmail: " + customer.email() + '\n'
                        + "\nDate Created: " + customer.createdAt())
                .setCancelable(false)
                .setNegativeButton("Close", R.drawable.ic_baseline_close_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Remove", R.drawable.ic_baseline_close_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        delete(customer.id());
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mDialog.show();
    }

    private void delete(String id) {
        DeleteCustomersInput input =DeleteCustomersInput.builder().id(id).build();

        DeleteCustomersMutation deleteCustomerMutation= DeleteCustomersMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(deleteCustomerMutation).enqueue(mutateCallbackDeleteCustomer);
    }

    private GraphQLCall.Callback<DeleteCustomersMutation.Data> mutateCallbackDeleteCustomer = new GraphQLCall.Callback<DeleteCustomersMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<DeleteCustomersMutation.Data> response) {
            Log.d("Delete", "delete success");
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            Log.e("", "Failed to perform DeleteItemMutation", e);
        }
    };

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
