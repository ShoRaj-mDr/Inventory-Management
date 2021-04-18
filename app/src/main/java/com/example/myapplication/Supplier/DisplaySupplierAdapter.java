package com.example.myapplication.Supplier;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListCustomerssQuery;
import com.amazonaws.amplify.generated.graphql.ListSupplierssQuery;
import com.example.myapplication.R;

import java.util.List;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;


public class DisplaySupplierAdapter extends RecyclerView.Adapter<DisplaySupplierAdapter.ViewHolder>{

    private final List<ListSupplierssQuery.Item> suppliers;
    private Context mContext;

    public DisplaySupplierAdapter(List<ListSupplierssQuery.Item> sup, Context context) {
        this.suppliers = sup;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DisplaySupplierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_display_supplier, parent, false);
        mContext = parent.getContext();
        return new DisplaySupplierAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplaySupplierAdapter.ViewHolder holder, int position) {
        final ListSupplierssQuery.Item item = suppliers.get(position);
        holder.supplierNumber.setText(Integer.toString(position + 1));
        holder.supplierName.setText(item.name());
        holder.supplierEmail.setText(item.email());

        String phoneNum = item.phone().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
        holder.supplierPhone.setText(phoneNum);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "Customer Name: " + customerinfo, Toast.LENGTH_SHORT).show();
                displayDialogBox(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    public void displayDialogBox(ListSupplierssQuery.Item supplier) {
        MaterialDialog mDialog = new MaterialDialog.Builder((Activity) mContext)
                .setTitle(supplier.name())
                .setMessage("ID: " + supplier.id() + '\n'
                        + "\nEmail: " + supplier.email() + '\n'
                        + "\nPhone: " + supplier.phone() + '\n'
                        + "\nAddress: " + supplier.address() + '\n')
                .setCancelable(false)
                .setNegativeButton("Close", R.drawable.ic_baseline_close_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mDialog.show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView supplierNumber;
        private final TextView supplierName;
        private final TextView supplierEmail;
        private final TextView supplierPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            supplierNumber = itemView.findViewById(R.id.supplier_number);
            supplierName = itemView.findViewById(R.id.supplier_name);
            supplierEmail = itemView.findViewById(R.id.supplier_email);
            supplierPhone = itemView.findViewById(R.id.supplier_phone);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Item: " + this.supplierName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
