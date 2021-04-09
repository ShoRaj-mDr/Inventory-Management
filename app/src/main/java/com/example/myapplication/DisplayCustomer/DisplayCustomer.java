package com.example.myapplication.DisplayCustomer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.ListCustomerssQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


public class DisplayCustomer extends AppCompatActivity {

    private List<ListCustomerssQuery.Item> customerList;
    private RecyclerView recyclerView;
    private DisplayCustomerAdapter mAdapter;

    private final String TAG = DisplayCustomer.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer);

        recyclerView = findViewById(R.id.customer_recyclerview);
        ClientFactory.init(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        ClientFactory.appSyncClient().query(ListCustomerssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallbackDisplayCustomer);
    }

    private final GraphQLCall.Callback<ListCustomerssQuery.Data> queryCallbackDisplayCustomer = new GraphQLCall.Callback<ListCustomerssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListCustomerssQuery.Data> response) {
            customerList = new ArrayList<>(response.data().listCustomerss().items());
            Log.i(TAG, "Retrieved list items: " + customerList.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(DisplayCustomer.this));
                    mAdapter = new DisplayCustomerAdapter(customerList);
                    recyclerView.setAdapter(mAdapter);
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
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