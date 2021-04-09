package com.example.myapplication.Orderlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateItemsMutation;
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

public class OrderList extends AppCompatActivity {

    private final List<Item> items = new ArrayList<>();
    private ItemAdapter mAdapter;
    private RecyclerView recyclerView;

    // Initializing Fab variables
    private TextView addProductTextView;
    private FloatingActionButton fab1_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

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
                if (!items.isEmpty()){
                    for (Item i: items){
                        addItemToDB(i);
                    }
                }
            }
        });

    }

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
//                    displayToast("Added Item");
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
//                    Toast.makeText(OrderList.this, "Failed to add item", Toast.LENGTH_SHORT).show();
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
