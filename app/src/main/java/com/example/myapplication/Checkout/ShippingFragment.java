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

import com.amazonaws.amplify.generated.graphql.CreateShippingMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;
import com.example.myapplication.currentUser;

import javax.annotation.Nonnull;

import type.CreateShippingInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class ShippingFragment extends Fragment {

    private TextView shippingName, shippingPhone, shippingAddress, shippingCity, shippingZip;
    Button from;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        shippingName = view.findViewById(R.id.shipping_full_name);
        shippingPhone = view.findViewById(R.id.shipping_phone_number);
        shippingAddress = view.findViewById(R.id.shipping_address);
        shippingCity = view.findViewById(R.id.shipping_city);
        shippingZip = view.findViewById(R.id.shipping_zip);

        // TODO: autoFill information if information is in the database
        //if(currentUser.id == shippingID) {
        //  setText()
        //}

        from = view.findViewById(R.id.shipping_to_payment);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerID = currentUser.id;
                String name = shippingName.getText().toString();
                String addr = shippingAddress.getText().toString();
                String phone = shippingPhone.getText().toString();
                String city = shippingCity.getText().toString();
                String zip = shippingZip.getText().toString();
                if(!name.equals("") && !addr.equals("") && !phone.equals("") && !city.equals("") && !zip.equals("")) {
                    if (phone.length() == 10 && zip.length() == 5) {
                        addShipping(customerID, name, addr, phone, city, zip);
                        // TODO: Need to set up update so that only 1 user can have 1 item in the db table

                        ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                        viewPager.setCurrentItem(1);
                    } else {
                        if (phone.length() != 10) {
                            shippingPhone.setError("Phone number does not exist!");
                        }
                        if (zip.length() != 5) {
                            shippingZip.setError("Zip Code does not exist!");
                        }
                    }
                } else {
//                    displayToast("Information is missing");
                    textviewEmpty(shippingName, name, "Name");
                    textviewEmpty(shippingAddress, addr, "Address");
                    textviewEmpty(shippingPhone, phone, "Phone");
                    textviewEmpty(shippingCity, city, "City");
                    textviewEmpty(shippingZip, zip, "Zip-Code");
                }
            }
        });

        return view;
    }

    private void addShipping(String userID, String name, String phone, String addr, String city, String zip) {
        CreateShippingInput input = CreateShippingInput.builder().userID(userID).name(name).phone(phone).address(addr).city(city).zip(zip).build();
        CreateShippingMutation addShippingMutation = CreateShippingMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addShippingMutation).enqueue(mutateCallbackShipping);
    }

    public void textviewEmpty(TextView tv, String s, String title) {
        String Error = title + " is missing!";
        if(TextUtils.isEmpty(s)) {
            tv.setError(Error);
            return;
        }
    }

    // Create item in the table
    private final GraphQLCall.Callback<CreateShippingMutation.Data> mutateCallbackShipping = new GraphQLCall.Callback<CreateShippingMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateShippingMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(this, "Added Supplier", Toast.LENGTH_SHORT).show();
                    //shiftActivity.this.finish();
                    displayToast("Added Shipping");
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddShippingMutation", e);
//                    Toast.makeText(registration_main.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    displayToast("Failed to add shipping");
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