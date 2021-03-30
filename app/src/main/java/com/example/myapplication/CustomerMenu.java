package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.UpdatePetMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.Profile.Profile;

import javax.annotation.Nonnull;

import type.UpdatePetInput;

public class CustomerMenu extends AppCompatActivity {
    private ListView employeeMenu_listView;
    private Toolbar employee_toolbar;

    private final String[] employeeMenu = {
            "Manage Inventory",
            "Ordering List",
            "Shift Management",
            "Suppliers",
            "Customers"
    };

    private final int[] employeeMenuImage = {
            R.drawable.employee_mainmenu_manage_inventory,
            R.drawable.employee_mainmenu_orderlist,
            R.drawable.employee_mainmenu_shift_management,
            R.drawable.employee_mainmenu_supplier,
            R.drawable.employee_mainmenu_customer
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        employeeMenu_listView = findViewById(R.id.employee_listView);
        employee_toolbar = findViewById(R.id.employee_toolbar);
        setSupportActionBar(employee_toolbar);


        EmployeeMenuAdapter adapter = new EmployeeMenuAdapter(getApplicationContext(), employeeMenu, employeeMenuImage);
        employeeMenu_listView.setAdapter(adapter);
        employeeMenu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {           // Manage Inventory
                    Intent startManageInventory = new Intent(CustomerMenu.this, myItems.class);
                    startActivity(startManageInventory);

                } else if (i == 1) {   // Order List
//                    Intent startOrderList = new Intent(EmployeeMenu.this, OrderList.class);
//                    startActivity(startOrderList);

                } else if (i == 2) {   // Shift Management
                    displayToast("Shift Management Clicked! ");

                } else if (i == 3) {   // Suppliers
//                    Intent startAddSupplier = new Intent(EmployeeMenu.this, add_supplier.class);
//                    startActivity(startAddSupplier);

                } else if (i == 4) {   // Customers
                    Intent startCustomer = new Intent(CustomerMenu.this, DisplayCustomer.class);
                    startActivity(startCustomer);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.dbmain_menu, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
                //Toast.makeText(myItems.this, "33333333333", Toast.LENGTH_LONG).show();
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
                //Toast.makeText(myItems.this, "77777777777777777777777777777", Toast.LENGTH_LONG).show();
                //setAdminView(currentUser.id,"employee");
                currentUser.employee=true;
                currentUser.customer=false;
                moveToAuthentication();


                return true;

            case R.id.item4:
                //Toast.makeText(myItems.this, "Bye2", Toast.LENGTH_LONG).show();
                AWSMobileClient.getInstance().signOut();
                //Bundle dataBundle = new Bundle();
                //dataBundle.putInt("id", 1);
                Intent i = new Intent(getApplicationContext(), AuthenticationActivity.class);
                //intent.putExtras(dataBundle);
                startActivity(i);
                //IdentityManager.getDefaultIdentityManager().signOut();

                return true;

            case R.id.item5:
                //Settings
                Intent profile = new Intent(getApplicationContext(), Profile.class);
                startActivity(profile);

//https://aws.amazon.com/blogs/mobile/using-android-sdk-with-amazon-cognito-your-user-pools/

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
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


    private void moveToAuthentication() {
        Intent i = new Intent(getApplicationContext(), AuthenticationActivity.class);
        startActivity(i);
    }

    //========================================================================================UPDATE
    private void setAdminView(String id, String view) {
        UpdatePetInput input = UpdatePetInput.builder().id(id).description(view).build();
        UpdatePetMutation updatePetMutation = UpdatePetMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(updatePetMutation).enqueue(mutateCallback4);
    }

    // Mutation callback code
    private GraphQLCall.Callback<UpdatePetMutation.Data> mutateCallback4 = new GraphQLCall.Callback<UpdatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdatePetMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CustomerMenu.this, "Updated admin view", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(CustomerMenu.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }
    };
}
