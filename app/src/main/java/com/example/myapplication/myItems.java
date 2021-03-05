package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.amplify.generated.graphql.ListItemzsQuery;
import com.amazonaws.amplify.generated.graphql.ListPetsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

public class myItems extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;

    private TextView expenseMain;
    private TextView savingsGoal;
    private TextView nameMain;
    private TextView txtDailySavings;


    private ArrayList<ListItemzsQuery.Item> mPets;
    private ArrayList<ListItemssQuery.Item> mItems;
    private final String TAG = myItems.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        expenseMain = findViewById(R.id.textView);
        savingsGoal = findViewById(R.id.txtSavingsGoal);
        nameMain = findViewById(R.id.textView2);
        txtDailySavings = findViewById(R.id.txtDailySavings);
        ClientFactory.init(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Query list data when we return to the screen
        query();
    }

    public void query() {
        ClientFactory.appSyncClient().query(ListItemssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<ListItemssQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemssQuery.Data> response) {

            //mPets = new ArrayList<>(response.data().listItemzs().items());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dbmain_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.item1:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 1);
                Intent intent = new Intent(getApplicationContext(), DisplayItems.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                return true;

            case R.id.item3:    // report 1 pie chart

                return true;

            case R.id.item6:    // report 2 line graph

                return true;

            case R.id.item7: // report 3

                return true;

            case R.id.item4:

                return true;

            case R.id.item5:


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void myFunc(List<ListItemssQuery.Item> items) {

     //This loop will concatenate all our item names with their associated price.
     final ArrayList lastAL = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            lastAL.add(i, items.get(i).name() + "    " + items.get(i).quantity());
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lastAL);

        obj = findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Bundle dataBundle = new Bundle();
                String itemID=mItems.get(position).id();
                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(), DisplayItems.class);
                intent.putExtra("itemID",mItems.get(position).id());
                intent.putExtra("itemName",mItems.get(position).name());
                intent.putExtra("itemDes",mItems.get(position).description());
                intent.putExtra("itemPrice",mItems.get(position).price());
                intent.putExtra("itemQuantity",mItems.get(position).quantity());

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}