package com.example.myapplication.Checkout;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.amazonaws.amplify.generated.graphql.CreateShippingMutation;
import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.amplify.generated.graphql.ListShippingsQuery;
import com.amazonaws.amplify.generated.graphql.UpdateItemsMutation;
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

import type.CreateShippingInput;
import type.UpdateItemsInput;
import type.UpdateShiftInput;
import type.UpdateShippingInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class ShippingFragment extends Fragment {

    private EditText shippingName, shippingPhone, shippingAddress, shippingCity, shippingZip;
    Button from;
    private boolean foundUser;
    private String id;

    private ArrayList<ListShippingsQuery.Item> mUsers;
    private ArrayList<ArrayList<String>> users = new ArrayList<ArrayList<String>>();
    private ArrayList<String> userIDs = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        ClientFactory.appSyncClient().query(ListShippingsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shippingName = view.findViewById(R.id.shipping_full_name);
        shippingPhone = view.findViewById(R.id.shipping_phone_number);
        shippingAddress = view.findViewById(R.id.shipping_address);
        shippingCity = view.findViewById(R.id.shipping_city);
        shippingZip = view.findViewById(R.id.shipping_zip);

        // TODO: autoFill information if information is in the database
        System.out.println("Trying to find if user is in shipping table.");
        for(int i = 0; i < userIDs.size(); i++) {
            if(currentUser.id.equals(userIDs.get(i))){
                System.out.println("USER IS FOUND");
                foundUser = true;
                shippingName.setText(users.get(i).get(1));
                shippingPhone.setText(users.get(i).get(2));
                shippingAddress.setText(users.get(i).get(3));
                shippingCity.setText(users.get(i).get(4));
                shippingZip.setText(users.get(i).get(5));
                id = users.get(i).get(6);
                break;
            } else { foundUser = false; }
        }

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
                    if(phone.length() == 10 && zip.length() == 5) {
                        if(foundUser) {
                            //UpdateItemsInput input= UpdateItemsInput.builder().id(id).name(name).description(description).price(price).quantity(quantity).build();
                            UpdateShippingInput input = UpdateShippingInput.builder().id(id).userID(customerID).name(name).phone(phone).address(addr).city(city).zip(zip).build();

                            UpdateShippingMutation updateShippingMutation = UpdateShippingMutation.builder().input(input).build();
                            ClientFactory.appSyncClient().mutate(updateShippingMutation).enqueue(mutateCallback3);
                            System.out.println("SUCCESS Updated" + name);
                        } else if (!foundUser){
                            addShipping(customerID, name, phone, addr, city, zip);
                            System.out.println("Added User Information: " + customerID + ", " + name + ", " + phone + ", " + addr + ", " + city + ", " + zip);
                        }

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

    // Gets data
    private final GraphQLCall.Callback<ListShippingsQuery.Data> queryCallback = new GraphQLCall.Callback<ListShippingsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListShippingsQuery.Data> response) {
            mUsers = new ArrayList<>(response.data().listShippings().items());

            System.out.println("TESTING PULLING Users' information");
            System.out.println(mUsers);
            for(int i = 0; i < mUsers.size(); i++) {
                ArrayList<String> tempList = new ArrayList<String>();
                tempList.add(mUsers.get(i).userID());
                tempList.add(mUsers.get(i).name());
                tempList.add(mUsers.get(i).phone());
                tempList.add(mUsers.get(i).address());
                tempList.add(mUsers.get(i).city());
                tempList.add(mUsers.get(i).zip());
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
    private final GraphQLCall.Callback<UpdateShippingMutation.Data> mutateCallback3 = new GraphQLCall.Callback<UpdateShippingMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateShippingMutation.Data> response) {

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