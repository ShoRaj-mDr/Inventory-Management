package com.example.myapplication.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.amplify.generated.graphql.UpdateItemsMutation;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.DisplayItems;
import com.example.myapplication.ShoppingCart.ShoppingCart;
import com.example.myapplication.Item.Item;
import com.example.myapplication.CustomerMenu;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import type.UpdateItemsInput;

public class ConfirmationFragment extends Fragment {

    TextView subtotalPrice,taxNum, totalNum;
    ArrayList<Item> shoppingCartList;
    Intent returnIntent;
    private int totalPrice = 0;
    private double taxPercentage = .0725;
    private String id, description, name;
    private double price;
    private int quantity;

    private ArrayList<ListItemssQuery.Item> mItems;
    private ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
    private ArrayList<String> itemName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation,container,false);

        ClientFactory.appSyncClient().query(ListItemssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shoppingCartList = new ArrayList<Item>();

        returnIntent = new Intent();
        shoppingCartList = getActivity().getIntent().getExtras().getParcelableArrayList("shoppingList");

        ListView listView = (ListView) view.findViewById(R.id.confirm_list_view);

        ConfirmListAdapter adapter = new ConfirmListAdapter(getActivity(), R.layout.list_confirmation, shoppingCartList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(null);

        for(int i = 0; i < shoppingCartList.size(); i++) {
            totalPrice += shoppingCartList.get(i).getPrice();
        }

        subtotalPrice = view.findViewById(R.id.subtotal_price);
        String subtotal = "$" + String.valueOf(totalPrice);
        subtotalPrice.setText(subtotal);

        taxNum = view.findViewById(R.id.tax_price);
        double temp = totalPrice * taxPercentage;
        BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
        double taxCalc = bd.doubleValue();
        String tax = "$" + taxCalc;
        taxNum.setText(String.valueOf(tax));

        totalNum = view.findViewById(R.id.total_price);
        double totalCalc = taxCalc + totalPrice;
        String total = "$" + totalCalc;
        totalNum.setText(String.valueOf(total));

        Button back = (Button) view.findViewById(R.id.confirmation_to_payment);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToast("Items are being processed and shipped");
                update(view);

                Intent mainMenu = new Intent( getActivity(), CustomerMenu.class);
                startActivity(mainMenu);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
            }
        });


        return view;
    }

    // Takes all the items in the inventory to get the name and quantity
    private final GraphQLCall.Callback<ListItemssQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemssQuery.Data> response) {
            mItems = new ArrayList<>(response.data().listItemss().items());
            System.out.println("TESTING PULLING ITEMS");
            System.out.println(mItems);
            for(int i = 0; i < mItems.size(); i++) {
                ArrayList<String> tempList = new ArrayList<String>();
                tempList.add(mItems.get(i).name());
                tempList.add(mItems.get(i).id());
                tempList.add(mItems.get(i).description());
                tempList.add(String.valueOf(mItems.get(i).price()));
                tempList.add(String.valueOf(mItems.get(i).quantity()));
                items.add(tempList);
            }

            itemName = new ArrayList<String>();
            for(int i = 0; i < items.size(); i++) {
                itemName.add(items.get(i).get(1));
            }
            System.out.println("TESTING IF ITEMS ARE HERE");
            System.out.println(itemName);
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            System.out.println("FAILURE");
        }
    };

    // Update
    private final GraphQLCall.Callback<UpdateItemsMutation.Data> mutateCallback3 = new GraphQLCall.Callback<UpdateItemsMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateItemsMutation.Data> response) {

        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {

        }
    };

    // Updates the inventory in the database
    private void update(View view) {
        System.out.println("PRINTING:");
        System.out.println(shoppingCartList);
        System.out.println(items);
        for(int i = 0; i < shoppingCartList.size(); i++) {
            for(int j = 0; j < itemName.size(); j++) {
                if(items.get(j).get(0).equals(shoppingCartList.get(i).getName())) {
                    name = items.get(j).get(0);
                    id = items.get(j).get(1);
                    description = items.get(j).get(2);
                    price = Double.parseDouble(items.get(j).get(3));
                    quantity = Integer.parseInt(items.get(j).get(4));
                    System.out.println(name + ", " + description + ", " + price + ", " + quantity);
                    quantity = quantity - shoppingCartList.get(i).getQuantity();
                    UpdateItemsInput input= UpdateItemsInput.builder().id(id).name(name).description(description).price(price).quantity(quantity).build();

                    UpdateItemsMutation updateItemsMutation= UpdateItemsMutation.builder().input(input).build();
                    ClientFactory.appSyncClient().mutate(updateItemsMutation).enqueue(mutateCallback3);
                    System.out.println("SUCCESS Updated" + name);
                }
            }
        }
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