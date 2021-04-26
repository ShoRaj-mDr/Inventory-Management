package com.example.myapplication.Checkout;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.amazonaws.amplify.generated.graphql.ListPaymentsQuery;
import com.amazonaws.amplify.generated.graphql.ListShippingsQuery;
import com.amazonaws.amplify.generated.graphql.UpdatePaymentMutation;
import com.amazonaws.amplify.generated.graphql.UpdateShippingMutation;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;
import com.example.myapplication.currentUser;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import type.CreatePaymentInput;
import type.CreateShippingInput;
import type.UpdatePaymentInput;
import type.UpdateShippingInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class PaymentFragment extends Fragment {

    private TextView paymentName, paymentCCNumber, paymentExp, paymentCvv;
    Button from, back;

    private boolean foundUser;
    private String id;

    private ArrayList<ListPaymentsQuery.Item> mUsers;
    private ArrayList<ArrayList<String>> users = new ArrayList<ArrayList<String>>();
    private ArrayList<String> userIDs = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment,container,false);

        ClientFactory.appSyncClient().query(ListPaymentsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        paymentName = view.findViewById(R.id.payment_name);
        paymentCCNumber = view.findViewById(R.id.payment_cc_number);
        paymentExp = view.findViewById(R.id.payment_expiration_date);
        paymentCvv = view.findViewById(R.id.payment_cvv);

        System.out.println("Trying to find if user is in payment table.");
        for(int i = 0; i < userIDs.size(); i++) {
            if(currentUser.id.equals(userIDs.get(i))){
                System.out.println("USER IS FOUND");
                foundUser = true;
                paymentName.setText(users.get(i).get(1));
                paymentCCNumber.setText(users.get(i).get(2));
                paymentExp.setText(users.get(i).get(3));
                paymentCvv.setText(users.get(i).get(4));
                id = users.get(i).get(5);
                break;
            } else { foundUser = false; }
        }

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
                    if(ccNum.length() == 16 && cvv.length() == 3) {
                        if(foundUser) {
                            UpdatePaymentInput input = UpdatePaymentInput.builder().id(id).userID(customerID).name(name).ccNumber(ccNum).exp(exp).cvv(cvv).build();

                            UpdatePaymentMutation updatePaymentMutation = UpdatePaymentMutation.builder().input(input).build();
                            ClientFactory.appSyncClient().mutate(updatePaymentMutation).enqueue(mutateCallback3);
                            System.out.println("SUCCESS Updated" + name);
                        } else {
                            addPayment(customerID, name, ccNum, exp, cvv);
                            System.out.println("Added User Information: " + customerID + ", " + name + ", " + ccNum + ", " + exp + ", " + cvv);
                        }
                    } else {
                        if (ccNum.length() != 16) {
                            paymentCCNumber.setError("Credit Card Number is invalid!");
                        }
                        if (cvv.length() != 3) {
                            paymentCvv.setError("CVV is invalid!");
                        }
                    }

                    ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                    viewPager.setCurrentItem(2);
                } else {
                    textviewEmpty(paymentName, name, "Name");
                    textviewEmpty(paymentCCNumber, ccNum, "Credit Card Number");
                    textviewEmpty(paymentExp, exp, "EXP");
                    textviewEmpty(paymentCvv, cvv, "CVV");
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

    public void textviewEmpty(TextView tv, String s, String title) {
        String Error = title + " is missing!";
        if(TextUtils.isEmpty(s)) {
            tv.setError(Error);
            return;
        }
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

    // Gets data
    private final GraphQLCall.Callback<ListPaymentsQuery.Data> queryCallback = new GraphQLCall.Callback<ListPaymentsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListPaymentsQuery.Data> response) {
            mUsers = new ArrayList<>(response.data().listPayments().items());

            System.out.println("TESTING PULLING Users' information");
            System.out.println(mUsers);
            for(int i = 0; i < mUsers.size(); i++) {
                ArrayList<String> tempList = new ArrayList<String>();
                tempList.add(mUsers.get(i).userID());
                tempList.add(mUsers.get(i).name());
                tempList.add(mUsers.get(i).ccNumber());
                tempList.add(mUsers.get(i).exp());
                tempList.add(mUsers.get(i).cvv());
                tempList.add(mUsers.get(i).id());

                users.add(tempList);
            }

            userIDs = new ArrayList<>();
            for(int i = 0; i < users.size(); i++) {
                userIDs.add(users.get(i).get(0));
            }
            System.out.println("TESTING IF ITEMS ARE HERE");
            System.out.println(userIDs);
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            System.out.println("FAILURE");
        }
    };

    // Update
    private final GraphQLCall.Callback<UpdatePaymentMutation.Data> mutateCallback3 = new GraphQLCall.Callback<UpdatePaymentMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdatePaymentMutation.Data> response) {

        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {

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