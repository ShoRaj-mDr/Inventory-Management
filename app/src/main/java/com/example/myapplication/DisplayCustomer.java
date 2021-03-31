package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.warehousemanagmentsystem491b.R;

//import com.example.myapplication.R;

//import com.example.myapplication.R;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DisplayCustomer extends AppCompatActivity {

    private ListView listView;
    private List<String> customerList;




    private final String TAG = myItems.class.getSimpleName();
    String s="something";
    // Try: DB stuff ~
    private ArrayList<ListItemssQuery.Item> mItems;
    private final GraphQLCall.Callback<ListItemssQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemssQuery.Data> response) {

            mItems = new ArrayList<>(response.data().listItemss().items());
            Log.i(TAG, "Retrieved list items: " + mItems.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myFunc(mItems);

                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    public void initializeList() {
        customerList.add("Customer 1");
        customerList.add("Customer 2");
        customerList.add("Customer 3");
        customerList.add("Customer 4");
        customerList.add("Customer 5");
        customerList.add("Customer 6");
        customerList.add("Customer 7");
        customerList.add("Customer 8");
        customerList.add("Customer 9");
        customerList.add("Customer 10");
        customerList.add("Customer 11");
        customerList.add("Customer 12");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer);

        listView = findViewById(R.id.customer_listview);
        customerList = new ArrayList<>();

//        initializeList();
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
//        listView.setAdapter(mAdapter);


        // Try: DB stuff ~
        ClientFactory.init(this);
        s = getIntent().getStringExtra("cogUser");

    }

    // Try: DB stuff ~
    @Override
    public void onResume() {
        super.onResume();
        ClientFactory.appSyncClient().query(ListItemssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    public void myFunc(List<ListItemssQuery.Item> items) {

        //This loop will concatenate all our item names with their associated price.
        final ArrayList lastAL = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            lastAL.add(i, items.get(i).name() + "    " + items.get(i).quantity());
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lastAL);

//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
//        listView.setAdapter(mAdapter);

        listView = findViewById(R.id.customer_listview);
        listView.setAdapter(arrayAdapter);
    }


}