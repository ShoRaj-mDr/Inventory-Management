package com.example.myapplication.Orderlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateItemsMutation;
import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.amplify.generated.graphql.ListShiftsQuery;
import com.amazonaws.amplify.generated.graphql.UpdateItemsMutation;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.DisplayItems;
import com.example.myapplication.Item.Item;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateItemsInput;
import type.UpdateItemsInput;

public class OrderList extends AppCompatActivity {

    private final List<Item> items = new ArrayList<>();
    private ArrayList<ListItemssQuery.Item> mItems;
    private ItemAdapter mAdapter;
    private RecyclerView recyclerView;
    private boolean match = false;

    // Initializing Fab variables
    private TextView addProductTextView;
    private FloatingActionButton fab1_main;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        toolbar = findViewById(R.id.orderlist_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fab1_main = findViewById(R.id.orderlist_FAB1);
        recyclerView = findViewById(R.id.orderlist_itemDisplay_recyclerview);

        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new VegaLayoutManager());
        mAdapter = new ItemAdapter(items);
        new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter)).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Button btn = findViewById(R.id.orderlist_confirm_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                if (!items.isEmpty()){
                    for (Item i: items){
                        for (ListItemssQuery.Item t: mItems){
                            if (i.getName().equalsIgnoreCase(t.name())){
                                Log.i("matching item name","matching item name" );
                                updateExistingItemQuantity(t.id(), t.quantity(), i.getQuantity());
                                match = true;
                                break;
                            }
                        }

                        if(match == true){
                            match = false;
                            if(items.size() == 1){
                                OrderList.this.finish();
                                break;
                            }
                            else if(count == items.size()  ){
                                OrderList.this.finish();
                            }
                            else{
                                continue;
                            }
                        }

                        addItemToDB(i);
                        count++;
                    }
                }
            }
        });

        aquireListOfPreExistingItems();
    }

    private void updateExistingItemQuantity(String id, int num1, int num2) {

        int total = num1 + num2;
        UpdateItemsInput input= UpdateItemsInput.builder().id(id).quantity(total).build();

        UpdateItemsMutation updateItemsMutation= UpdateItemsMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(updateItemsMutation).enqueue(mutateCallbackItemUpdateOnOrderList);
    }

    // Mutation callback code
    private final GraphQLCall.Callback<UpdateItemsMutation.Data> mutateCallbackItemUpdateOnOrderList = new GraphQLCall.Callback<UpdateItemsMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateItemsMutation.Data> response) {
            Log.e("update order to iterm"," updated item quantity");
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            Log.i("update order to iterm","failed to update");
        }
    };

    private void aquireListOfPreExistingItems() {
        ClientFactory.appSyncClient().query(ListItemssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallbackExistingItems);
    }

    private final GraphQLCall.Callback<ListItemssQuery.Data> queryCallbackExistingItems = new GraphQLCall.Callback<ListItemssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemssQuery.Data> response) {
            mItems = new ArrayList<>(response.data().listItemss().items());
            Log.i("mItemsInOrderlist", "Retrieved list items: " + mItems.toString());


        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("mItemsInOrderlist", e.toString());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult");
        if (resultCode == RESULT_OK && data.getParcelableExtra("item") != null) {
            Log.d("TAG", "onActivityResult: OK");
            Item item = data.getParcelableExtra("item");
            items.add(item);
            mAdapter.notifyDataSetChanged();
        } else {
            Log.d("TAG", "onActivityResult: ERROR");
        }
    }

    public void fab1_main_onClick(View view) {
        Intent intentOrderItem = new Intent(this, CreateItem.class);
        startActivityForResult(intentOrderItem, 1);
    }

    private void addItemToDB(Item item) {
        final String name = item.getName();
        // long id = item.getId();
        final Double price = item.getPrice();
        final String descr = item.getDescription();
        final int quantity = item.getQuantity();

        Log.i("Item from orderlist: ", name);
        Log.i("Item from orderlist: ", String.valueOf(price));
        Log.i("Item from orderlist: ", descr);
        Log.i("Item from orderlist: ", String.valueOf(quantity));

        CreateItemsInput input = CreateItemsInput.builder().name(name).description(descr).price(price).quantity(quantity).build();
        CreateItemsMutation addItemMutation = CreateItemsMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addItemMutation).enqueue(mutateCallbackOrderlist);
    }

    private final GraphQLCall.Callback<CreateItemsMutation.Data> mutateCallbackOrderlist = new GraphQLCall.Callback<CreateItemsMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateItemsMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG: ", "Item Successfully added to database");
                    OrderList.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    OrderList.this.finish();
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
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

}


/*
    Resources used:
        RecyclerView: https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
        Search bar: https://www.journaldev.com/12478/android-searchview-example-tutorial
        FAB tool: https://medium.com/@shubham_nikam/easy-way-to-add-minimal-expandable-floating-action-button-fab-menu-dd8e6e011f52 -> (Used)
                                    OR
                  https://github.com/nambicompany/expandable-fab


    RecyclerView Designs:
        https://github.com/jiang111/Awesome-RecyclerView-LayoutManager
 */
