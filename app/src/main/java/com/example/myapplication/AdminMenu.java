package com.example.myapplication;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazonaws.amplify.generated.graphql.CreatePetMutation;
import com.amazonaws.amplify.generated.graphql.UpdatePetMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.DisplayCustomer.DisplayCustomer;
import com.example.myapplication.Orderlist.OrderList;
import com.example.myapplication.Profile.Profile;
import com.example.myapplication.Supplier.AddSupplier;
import com.jakewharton.processphoenix.ProcessPhoenix;

import javax.annotation.Nonnull;

import type.CreatePetInput;
import type.UpdatePetInput;

public class AdminMenu extends AppCompatActivity {

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
                Toast.makeText(AdminMenu.this, "Bye2", Toast.LENGTH_LONG).show();
                AWSMobileClient.getInstance().signOut();
                //item.notifyAll();
                //AWSMobileClient.getInstance().signOut(SignOut)
//                AWSMobileClient.getInstance().signOut(SignOutOptions.builder().signOutGlobally(true).build(), new Callback<Void>() {
//                    @Override
//                    public void onResult(final Void result) {
//                        Log.d("dddddd", "signed-out");
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.e(TAG, "sign-out error", e);
//                    }
//                });

               // AWSMobileClient.getInstance().get
                currentUser.admin=false;
                currentUser.loggingOut=true;
                //Bundle dataBundle = new Bundle();
                //dataBundle.putInt("id", 1);
                Intent i = new Intent(getApplicationContext(), AuthenticationActivity.class);
//                //intent.putExtras(dataBundle);
                startActivity(i);
                //ProcessPhoenix.triggerRebirth(AuthenticationActivity.class);

//                Intent nextIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
//                        ProcessPhoenix.triggerRebirth(AdminMenu.this, nextIntent);
                //moveToAuthentication();
                //IdentityManager.getDefaultIdentityManager().signOut();

                return true;

            case R.id.item5:
                //Profile
//                displayToast("ID = " + currentUser.id +
//                        "\nUsername = " + currentUser.name +
//                        "\nEmail = " + currentUser.email);
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
    //=============================================================================CREATE
    private void save() {
//        final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
//        final String description = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
//        final double price=Double.parseDouble(((EditText) findViewById(R.id.editTextStreet)).getText().toString());
//        final int quantity=Integer.parseInt(((EditText) findViewById(R.id.editTextEmail)).getText().toString());

        CreatePetInput input = CreatePetInput.builder()
                .id(currentUser.id)
                .name(currentUser.name)
                .description("admin")
                .build();
        //CreateItemsInput input = CreateItemsInput.builder().name(name).description(description).price(price).quantity(quantity).build();
        CreatePetMutation addPetMutation = CreatePetMutation.builder()
                .input(input)
                .build();
        //CreateItemsMutation addItemMutation = CreateItemsMutation.builder().input(input).build();
        //ClientFactory.appSyncClient().mutate(addItemMutation).enqueue(mutateCallback22);
        ClientFactory.appSyncClient().mutate(addPetMutation).enqueue(mutateCallback22);
    }

    // Mutation callback code
    private final GraphQLCall.Callback<CreatePetMutation.Data> mutateCallback22 = new GraphQLCall.Callback<CreatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreatePetMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(DisplayItems.this, "Added Item", Toast.LENGTH_SHORT).show();
                    // DisplayItems.this.finish();
                    Log.i("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS", "SSSSSSSSSSSSSSSSSSSSSSSSSSS");
                    currentUser.admin=true;
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    //Toast.makeText(DisplayItems.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }
    };

    //============================================================================================= CREATE
    //======================================================================================== UPDATE
    private void setAdminView(String id, String view) {
        UpdatePetInput input = UpdatePetInput.builder().id(id).description(view).build();
        UpdatePetMutation updatePetMutation = UpdatePetMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(updatePetMutation).enqueue(mutateCallback4);
    }


    // Mutation callback code
    private final GraphQLCall.Callback<UpdatePetMutation.Data> mutateCallback4 = new GraphQLCall.Callback<UpdatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdatePetMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(CustomerMenu.this, "Updated admin view", Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(CustomerMenu.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }
    };
    //======================================================================================== UPDATE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        employeeMenu_listView = findViewById(R.id.employee_listView);
        employee_toolbar = findViewById(R.id.create_item_toolbar);
        setSupportActionBar(employee_toolbar);

        // Removing back button navigation from all main menu
        /* employee_toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        employee_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        // save();
        EmployeeMenuAdapter adapter = new EmployeeMenuAdapter(getApplicationContext(), employeeMenu, employeeMenuImage);
        employeeMenu_listView.setAdapter(adapter);
        employeeMenu_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {           // Manage Inventory
                    Intent startManageInventory = new Intent(AdminMenu.this, myItems.class);
                    startActivity(startManageInventory);

                } else if (i == 1) {   // Order List
                    Intent startOrderList = new Intent(AdminMenu.this, OrderList.class);
                    startActivity(startOrderList);

                } else if (i == 2) {   // Shift Management
                    displayToast("Shift Management Clicked! ");

                } else if (i == 3) {   // Suppliers
                    Intent startAddSupplier = new Intent(AdminMenu.this, AddSupplier.class);
                    startActivity(startAddSupplier);

                } else if (i == 4) {   // Customers
                    Intent startCustomer = new Intent(AdminMenu.this, DisplayCustomer.class);
                    startActivity(startCustomer);

                }
            }
        });

    }

}


