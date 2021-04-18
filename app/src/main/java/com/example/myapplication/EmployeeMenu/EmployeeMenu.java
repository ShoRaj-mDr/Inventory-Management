package com.example.myapplication.EmployeeMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazonaws.amplify.generated.graphql.UpdatePetMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.AuthenticationActivity;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.DisplayCustomer.DisplayCustomer;
import com.example.myapplication.DisplayItems;
import com.example.myapplication.Inventory.ManageInventory;
import com.example.myapplication.Orderlist.OrderList;
import com.example.myapplication.R;
import com.example.myapplication.Supplier.AddSupplier;
import com.example.myapplication.currentUser;

import javax.annotation.Nonnull;

import type.UpdatePetInput;


public class EmployeeMenu extends AppCompatActivity {

    private ListView employeeMenu_listView;
    private Toolbar toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_menu);

        toolbar = findViewById(R.id.create_item_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onClickInventoryManagement(View view) {
        Intent startManageInventory = new Intent(EmployeeMenu.this, ManageInventory.class);
        startActivity(startManageInventory);
    }

    public void onClickOrderList(View view) {
        Intent startOrderList = new Intent(EmployeeMenu.this, OrderList.class);
        startActivity(startOrderList);
    }

    public void onClickShiftManagement(View view) {
        displayToast("Shift Management Clicked! ");
    }

    public void onClickSuppliers(View view) {
        Intent startAddSupplier = new Intent(EmployeeMenu.this, AddSupplier.class);
        startActivity(startAddSupplier);
    }

    public void onClickCustomers(View view) {
        Intent startCustomer = new Intent(EmployeeMenu.this, DisplayCustomer.class);
        startActivity(startCustomer);
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
    private final GraphQLCall.Callback<UpdatePetMutation.Data> mutateCallback4 = new GraphQLCall.Callback<UpdatePetMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdatePetMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EmployeeMenu.this, "Updated admin view", Toast.LENGTH_LONG).show();
                    // DisplayItems.this.finish();
//                    moveToAuthentication();

                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform UpdateItemMutation", e);
                    Toast.makeText(EmployeeMenu.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }
    };

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
                Toast.makeText(EmployeeMenu.this, "77777777777777777777777777777", Toast.LENGTH_LONG).show();
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
                currentUser.admin=false;
                currentUser.loggingOut=true;
                Intent i = new Intent(getApplicationContext(), AuthenticationActivity.class);
                //intent.putExtras(dataBundle);
                startActivity(i);
                //IdentityManager.getDefaultIdentityManager().signOut();

                return true;

            case R.id.item5:
// https://aws.amazon.com/blogs/mobile/using-android-sdk-with-amazon-cognito-your-user-pools/
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
//=============================================================================UPDATE

/*
     Website Referenced:
         - https://www.javatpoint.com/android-custom-listview
         - https://stackoverflow.com/questions/19662233/how-open-new-activity-clicking-an-item-in-listview
 */
