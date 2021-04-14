package com.example.myapplication.Checkout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.amazonaws.amplify.generated.graphql.CreatePaymentMutation;
import com.amazonaws.amplify.generated.graphql.CreateShippingMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;
import com.example.myapplication.currentUser;

import javax.annotation.Nonnull;

import type.CreatePaymentInput;
import type.CreateShippingInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class PaymentFragment extends Fragment {

    private TextView paymentName, paymentCCNumber, paymentExp, paymentCvv;
    Button from, back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment,container,false);

        paymentName = view.findViewById(R.id.payment_name);
        paymentCCNumber = view.findViewById(R.id.payment_cc_number);
        paymentExp = view.findViewById(R.id.payment_expiration_date);
        paymentCvv = view.findViewById(R.id.payment_cvv);

        from = view.findViewById(R.id.payment_to_confirmation);
        back = view.findViewById(R.id.payment_to_shipping);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerID = currentUser.id;
                String name = paymentName.getText().toString();
                String ccNum = paymentCCNumber.getText().toString();
                String exp = paymentExp.getText().toString();
                String cvv = paymentCvv.getText().toString();
                if(!name.equals("") && !ccNum.equals("") && !exp.equals("") && !cvv.equals("")) {
                    addPayment(customerID, name, ccNum, exp, cvv);
                    // TODO: Need to set up update so that only 1 user can have 1 item in the db table

                    ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                    viewPager.setCurrentItem(2);
                } else {
                    displayToast("Information is missing");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(0);
            }
        });

        return view;
    }

    private void addPayment(String userID, String name, String ccNum, String exp, String cvv) {
        CreatePaymentInput input = CreatePaymentInput.builder().userID(userID).name(name).ccNumber(ccNum).exp(exp).cvv(cvv).build();
        CreatePaymentMutation addPaymentMutation = CreatePaymentMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addPaymentMutation).enqueue(mutateCallbackPayment);
    }

    // Create item in the table
    private final GraphQLCall.Callback<CreatePaymentMutation.Data> mutateCallbackPayment = new GraphQLCall.Callback<CreatePaymentMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreatePaymentMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(this, "Added Supplier", Toast.LENGTH_SHORT).show();
                    //shiftActivity.this.finish();
                    displayToast("Added Payment");
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPaymentMutation", e);
//                    Toast.makeText(registration_main.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    displayToast("Failed to add payment");
                    //shiftActivity.this.finish();
                }
            });
        }
    };

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