package com.example.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.amazonaws.amplify.generated.graphql.CreateItemsMutation;
import com.amazonaws.amplify.generated.graphql.DeleteItemsMutation;
import com.amazonaws.amplify.generated.graphql.UpdateItemsMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateItemsInput;
import type.DeleteItemsInput;
import type.UpdateItemsInput;

public class DisplayItems extends Activity {

    // Initialize all the fields
    private TextView name ;
    private TextView descriptiontxt;
    private TextView currentExpense;
    private TextView newExpense;

    private String itemID;
    private String itemName;
    private String itemDescription;
    private int itemQuantity;
    private double itemPrice;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);

        // This thing will crash the app, cuz both employee menu and admin menu uses this with different layouts
         /* toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); */


        // Initialize Fields
        name = findViewById(R.id.editTextName);
        descriptiontxt = findViewById(R.id.editTextPhone);
        currentExpense = findViewById(R.id.editTextStreet);
        newExpense = findViewById(R.id.editTextEmail);

        Bundle extras = getIntent().getExtras();

        //We passed all data associated with an item with intents.
        //Here we grab all those intents and assign them to local variables.
        itemID = getIntent().getStringExtra("itemID");
        itemName = getIntent().getStringExtra("itemName");
        itemDescription = getIntent().getStringExtra("itemDes");
        itemPrice=getIntent().getDoubleExtra("itemPrice",0);
        itemQuantity=getIntent().getIntExtra("itemQuantity",0);


        //If Value > 0 we are creating an item
        //If Value == 0 we are updating an item
        int Value = extras.getInt("id");
        //We are creating a new item, all entries are blank.
        if (Value > 0) {

            name.setFocusable(true);
            name.setClickable(true);

            descriptiontxt.setEnabled(true);
            descriptiontxt.setFocusableInTouchMode(true);
            descriptiontxt.setClickable(true);

            currentExpense.setFocusable(true);
            currentExpense.setClickable(true);

            newExpense.setEnabled(true);
            newExpense.setFocusableInTouchMode(true);
            newExpense.setClickable(true);
        }
        //We are updating an item
        else {
            String strStreet = "";

            Button b = findViewById(R.id.updateItemButton);
            name.setText(itemName);
            name.setFocusable(true);
            name.setClickable(true);

            descriptiontxt.setText(itemDescription);
            descriptiontxt.setEnabled(true);
            descriptiontxt.setFocusableInTouchMode(true);
            descriptiontxt.setClickable(true);

            currentExpense.setText(String.valueOf(itemPrice));
            currentExpense.setFocusable(true);
            currentExpense.setClickable(true);

            newExpense.setText(String.valueOf(itemQuantity));
            newExpense.setEnabled(true);
            newExpense.setFocusableInTouchMode(true);
            newExpense.setClickable(true);
        }

    }
    public void add(View view) { //+1
        itemQuantity++;//++ quantity, no DB update until save button is pressed.
        newExpense.setText(String.valueOf(itemQuantity));//Display new quantity
    }
    public void sub(View view) { //-1
        if(itemQuantity!=0) {//Make sure we don't have negative items.
            itemQuantity--;//--quantity, no DB update until save button is pressed.
            newExpense.setText(String.valueOf(itemQuantity));////Display new quantity
        }
    }
    //run and run2 are tied to the buttons in the layout file
    //Lines 154 and 167 in the activity_display_items.xml file
    public void run2(View view) { //DELETE ITEM
        Toast.makeText(getApplicationContext(), "Deleting an item",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), myItems.class);
        delete(itemID);
        startActivity(intent);
    }

    public void run(View view) {//ADD ITEM
        Bundle extras = getIntent().getExtras();
        int Value = extras.getInt("id");
        //If value > 0 then we are creating a brand new item
        if (Value > 0) {
            save();
            Intent intent = new Intent(getApplicationContext(), myItems.class);
            startActivity(intent);
        } else {//Otherwise we are updating an item.
            update(itemID);
            Intent intent = new Intent(getApplicationContext(), myItems.class);
            startActivity(intent);
        }
    }
    //========================================================================================UPDATE
    private void update(String id) {
        final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        final String description = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
        final double price = Double.parseDouble(((EditText) findViewById(R.id.editTextStreet)).getText().toString());
        final int quantity=Integer.parseInt(((EditText) findViewById(R.id.editTextEmail)).getText().toString());

        UpdateItemsInput input= UpdateItemsInput.builder().id(id).name(name).description(description).price(price).quantity(quantity).build();

        UpdateItemsMutation updateItemsMutation= UpdateItemsMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(updateItemsMutation).enqueue(mutateCallback3);
    }

    // Mutation callback code
    private final GraphQLCall.Callback<UpdateItemsMutation.Data> mutateCallback3 = new GraphQLCall.Callback<UpdateItemsMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateItemsMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DisplayItems.this, "Updated Item", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform UpdateItemMutation", e);
                    Toast.makeText(DisplayItems.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }
    };
    //=============================================================================UPDATE


    //========================================================================================DELETE
    private void delete(String id) {
        DeleteItemsInput input =DeleteItemsInput.builder().id(id).build();
        DeleteItemsMutation deleteItemMutation= DeleteItemsMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(deleteItemMutation).enqueue(mutateCallback2);
    }

    // Mutation callback code
    private final GraphQLCall.Callback<DeleteItemsMutation.Data> mutateCallback2 = new GraphQLCall.Callback<DeleteItemsMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<DeleteItemsMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DisplayItems.this, "Deleted Item", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform DeleteItemMutation", e);
                    Toast.makeText(DisplayItems.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }
    };
    //=============================================================================DELETE

    //=============================================================================CREATE
    private void save() {
        final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        final String description = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
        final double price=Double.parseDouble(((EditText) findViewById(R.id.editTextStreet)).getText().toString());
        final int quantity=Integer.parseInt(((EditText) findViewById(R.id.editTextEmail)).getText().toString());

//        CreatePetInput input = CreatePetInput.builder()
//                .name(name)
//                .description(description)
//                .build();
        CreateItemsInput input = CreateItemsInput.builder().name(name).description(description).price(price).quantity(quantity).build();
//        CreatePetMutation addPetMutation = CreatePetMutation.builder()
//                .input(input)
//                .build();
        CreateItemsMutation addItemMutation = CreateItemsMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addItemMutation).enqueue(mutateCallback);
    }

    // Mutation callback code
    private final GraphQLCall.Callback<CreateItemsMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateItemsMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateItemsMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DisplayItems.this, "Added Item", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    Toast.makeText(DisplayItems.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }
    };
//=============================================================================================CREATE
}
