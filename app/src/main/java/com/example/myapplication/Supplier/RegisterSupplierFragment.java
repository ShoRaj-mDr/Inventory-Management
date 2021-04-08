package com.example.myapplication.Supplier;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amazonaws.amplify.generated.graphql.CreateSuppliersMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;

import javax.annotation.Nonnull;

import type.CreateSuppliersInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class RegisterSupplierFragment extends Fragment {

    private TextView supplierName, supplierEmail, supplierAddress, supplierPhone;
    private Button addSupplierBtn;

    public RegisterSupplierFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_supplier, container, false);

        supplierName = view.findViewById(R.id.supplier_name);
        supplierEmail = view.findViewById(R.id.supplier_email);
        supplierAddress = view.findViewById(R.id.supplier_shipping_address);
        supplierPhone = view.findViewById(R.id.supplier_phone);

        addSupplierBtn = view.findViewById(R.id.supplier_confirm_button);
        addSupplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = supplierName.getText().toString();
                String email = supplierEmail.getText().toString();
                String addr = supplierAddress.getText().toString();
                String phone = supplierPhone.getText().toString();

                addSupplier(name, email, addr, phone);

                supplierName.setText("");
                supplierEmail.setText("");
                supplierAddress.setText("");
                supplierPhone.setText("");
            }
        });

        return view;
    }

    private final GraphQLCall.Callback<CreateSuppliersMutation.Data> mutateCallbackSupplier = new GraphQLCall.Callback<CreateSuppliersMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateSuppliersMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(this, "Added Supplier", Toast.LENGTH_SHORT).show();
                    //shiftActivity.this.finish();
                    displayToast("Added Supplier");
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddSupplierMutation", e);
//                    Toast.makeText(registration_main.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    displayToast("Failed to add supplier");
                    //shiftActivity.this.finish();
                }
            });
        }
    };

    private void addSupplier(String name, String email, String addr, String phone) {
        CreateSuppliersInput input = CreateSuppliersInput.builder().name(name).email(email).address(addr).phone(phone).build();
        CreateSuppliersMutation addSupplierMutation = CreateSuppliersMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addSupplierMutation).enqueue(mutateCallbackSupplier);
    }

    /**
     * Displays a Toast with the message.
     *
     * @param message Message to display
     */
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_SHORT).show();
    }




}