package com.example.myapplication.Inventory;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;
import com.example.myapplication.myItems;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ManageInventory extends AppCompatActivity {

    private ArrayList<ListItemssQuery.Item> mItems;
    private RecyclerView recyclerView;
    private InventoryAdapter mAdapter;

    private final String TAG = myItems.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);

        recyclerView = findViewById(R.id.manageInventory_recyclerView);
        ClientFactory.init(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        ClientFactory.appSyncClient().query(ListItemssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private final GraphQLCall.Callback<ListItemssQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemssQuery.Data> response) {
            mItems = new ArrayList<>(response.data().listItemss().items());
            Log.i(TAG, "Retrieved list items: " + mItems.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayInventory(mItems);
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    public void displayInventory(List<ListItemssQuery.Item> items) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InventoryAdapter(mItems);
        recyclerView.setAdapter(mAdapter);
    }


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
