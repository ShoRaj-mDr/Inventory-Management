package com.example.myapplication;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.amplify.generated.graphql.UpdatePetMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.EmployeeMenu.EmployeeMenu;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.UpdatePetInput;

public class myItems extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;

    private TextView expenseMain;
    private TextView savingsGoal;
    private TextView nameMain;
    private TextView txtDailySavings;

    private ArrayList<ListItemssQuery.Item> mItems;
    private final String TAG = myItems.class.getSimpleName();
    private final GraphQLCall.Callback<ListItemssQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemssQuery.Data>() {
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
    // Mutation callback code
    private final GraphQLCall.Callback<UpdatePetMutation.Data> mutateCallback4 = new GraphQLCall.Callback<UpdatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdatePetMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(myItems.this, "Updated admin view", Toast.LENGTH_LONG).show();
                    //DisplayItems.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform UpdateItemMutation", e);
                    Toast.makeText(myItems.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }
    };

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
    String s = "something";


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
                Toast.makeText(myItems.this, "33333333333", Toast.LENGTH_LONG).show();
                currentUser.customer=false;
                currentUser.employee=false;
                moveToAuthentication();

                return true;

            case R.id.item6:    //Customer
                //setAdminView(currentUser.id,"customer");
                currentUser.customer=true;
                currentUser.employee=false;
                moveToAuthentication();
                return true;

            case R.id.item7: // Employee
                Toast.makeText(myItems.this, "77777777777777777777777777777", Toast.LENGTH_LONG).show();
                //setAdminView(currentUser.id,"employee");
                currentUser.employee=true;
                currentUser.customer=false;
                moveToAuthentication();


                return true;

            case R.id.item4:
                Toast.makeText(myItems.this, "Bye2", Toast.LENGTH_LONG).show();
                AWSMobileClient.getInstance().signOut();
                //Bundle dataBundle = new Bundle();
                //dataBundle.putInt("id", 1);
                Intent i = new Intent(getApplicationContext(), AuthenticationActivity.class);
                //intent.putExtras(dataBundle);
                startActivity(i);
                //IdentityManager.getDefaultIdentityManager().signOut();

                return true;

            case R.id.item5:


//https://aws.amazon.com/blogs/mobile/using-android-sdk-with-amazon-cognito-your-user-pools/

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void moveToAuthentication(){
        Intent i = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(i);
    }
    //========================================================================================UPDATE
    private void setAdminView(String id,String view) {
        UpdatePetInput input=UpdatePetInput.builder().id(id).description(view).build();
        UpdatePetMutation updatePetMutation= UpdatePetMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(updatePetMutation).enqueue(mutateCallback4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        expenseMain = findViewById(R.id.textView);
        savingsGoal = findViewById(R.id.txtSavingsGoal);
        nameMain = findViewById(R.id.textView2);
        txtDailySavings = findViewById(R.id.txtDailySavings);

        ClientFactory.init(this);
        s = getIntent().getStringExtra("cogUser");

        Toast.makeText(myItems.this, s, Toast.LENGTH_SHORT).show();


    }
    //=============================================================================UPDATE
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
            if(currentUser.employee) {
                Intent i = new Intent(myItems.this, EmployeeMenu.class);
                startActivity(i);
            }
            else if(currentUser.customer) {
                Intent i = new Intent(myItems.this, CustomerMenu.class);
                startActivity(i);
            }
            else{
                Intent i = new Intent(myItems.this, AdminMenu.class);
                startActivity(i);
            }
        }
        return super.onKeyDown(keycode, event);
    }
}