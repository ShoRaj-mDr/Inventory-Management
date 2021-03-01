package com.example.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateItemzMutation;
import com.amazonaws.amplify.generated.graphql.CreatePetMutation;
import com.amazonaws.amplify.generated.graphql.DeleteItemzMutation;
import com.amazonaws.amplify.generated.graphql.UpdateItemzMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import type.CreateItemzInput;
import type.CreatePetInput;
import type.DeleteItemzInput;
import type.UpdateItemzInput;

public class DisplayItems extends Activity {
    //All the fields
    private TextView name ;
    private TextView descriptiontxt;
    private TextView currentExpense;
    private TextView newExpense;

    int ourID;//Probably delete
    String itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);
        //Initialize Fields
        name = findViewById(R.id.editTextName);
        descriptiontxt = findViewById(R.id.editTextPhone);
        currentExpense = findViewById(R.id.editTextStreet);
        newExpense = findViewById(R.id.editTextEmail);

        Bundle extras = getIntent().getExtras();
        int exp = 0;//Default value for a field---Delete later

        //We passed all data associated with an item with intents.
        //Here we grab all those intents and assign them to local variables.
        ourID = getIntent().getIntExtra("ourID", 0);//Probably Delete
        itemID = getIntent().getStringExtra("itemID");
        String itemName = getIntent().getStringExtra("itemName");
        String itemDescription = getIntent().getStringExtra("itemDes");

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
        //We are updating an item, set current data as hints so we don't have to backspace.
        else {
            String strStreet = "";

            Button b = (Button) findViewById(R.id.button1);
            name.setText(itemName);
            name.setFocusable(true);
            name.setClickable(true);

            descriptiontxt.setText(itemDescription);
            descriptiontxt.setEnabled(true);
            descriptiontxt.setFocusableInTouchMode(true);
            descriptiontxt.setClickable(true);

            currentExpense.setText(String.valueOf(exp));
            currentExpense.setFocusable(true);
            currentExpense.setClickable(true);

            newExpense.setText((CharSequence) strStreet);
            newExpense.setEnabled(true);
            newExpense.setFocusableInTouchMode(true);
            newExpense.setClickable(true);
        }

    }

    /**
     * To delete the item from the list
     *
     * @param view
     */
    //run and run2 are tied to the buttons in the layout file
    //Lines 154 and 167 in the activity_display_items.xml file
    public void run2(View view) { //DELETE ITEM
        Toast.makeText(getApplicationContext(), "Deleting an item",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), myItems.class);
        intent.putExtra("itemid", ourID);//Might delete later
        Bundle extras = getIntent().getExtras();//Might delete later
        delete(itemID);
        startActivity(intent);
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        int Value = extras.getInt("id");
        //If value > 0 then we are creating a brand new item
        if (Value > 0) {
            save();


            Intent intent = new Intent(getApplicationContext(), myItems.class);
            intent.putExtra("itemid", ourID);


//                        Toast.makeText(getApplicationContext(), "Our budget has been surpassed ",
//                                Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else {
            update(itemID);
            Intent intent = new Intent(getApplicationContext(), myItems.class);
            intent.putExtra("itemid", ourID);
            startActivity(intent);
        }
    }

    /**
     * Get a String of Today Date in a String Fromat
     *
     * @return date in YYYY-MM-DD format
     */
    public String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(dNow);
    }
    //========================================================================================UPDATE
    private void update(String id) {
        final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        final String description = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();

        UpdateItemzInput input= UpdateItemzInput.builder().id(id).name(name).description(description).build();

        UpdateItemzMutation updateItemzMutation= UpdateItemzMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(updateItemzMutation).enqueue(mutateCallback3);
    }

    // Mutation callback code
    private GraphQLCall.Callback<UpdateItemzMutation.Data> mutateCallback3 = new GraphQLCall.Callback<UpdateItemzMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateItemzMutation.Data> response) {
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
        DeleteItemzInput input =DeleteItemzInput.builder().id(id).build();

        DeleteItemzMutation deleteItemMutation= DeleteItemzMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(deleteItemMutation).enqueue(mutateCallback2);
    }

    // Mutation callback code
    private GraphQLCall.Callback<DeleteItemzMutation.Data> mutateCallback2 = new GraphQLCall.Callback<DeleteItemzMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<DeleteItemzMutation.Data> response) {
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

//        CreatePetInput input = CreatePetInput.builder()
//                .name(name)
//                .description(description)
//                .build();
        CreateItemzInput input = CreateItemzInput.builder().name(name).description(description).build();
//        CreatePetMutation addPetMutation = CreatePetMutation.builder()
//                .input(input)
//                .build();
        CreateItemzMutation addItemMutation = CreateItemzMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addItemMutation).enqueue(mutateCallback);
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateItemzMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateItemzMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateItemzMutation.Data> response) {
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



//====================IGNORE

/**
 *  This is a action-bar menu on top
 *
 * @param menu
 * @return boolean true
 */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        Bundle extras = getIntent().getExtras();
//        if(extras !=null) {
//            int Value = extras.getInt("id");
//            if(Value>0){
//                getMenuInflater().inflate(R.menu.display_contact, menu);
//            } else{
//                getMenuInflater().inflate(R.menu.dbmain_menu, menu);
//            }
//        }
//        return true;
//    }
//
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//
//        switch(item.getItemId()) {
//            case R.id.Edit_Contact:
//                Button b = (Button)findViewById(R.id.button1);
//                b.setVisibility(View.VISIBLE);
//                Button c = findViewById(R.id.button);
//
//                // feature: enables or disables when required
//                c.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        p = 1;
//                    }
//                });
//                name.setEnabled(true);
//                name.setFocusableInTouchMode(true);
//                name.setClickable(true);
//
//                descriptiontxt.setEnabled(true);
//                descriptiontxt.setFocusableInTouchMode(true);
//                descriptiontxt.setClickable(true);
//
//                currentExpense.setEnabled(true);
//                currentExpense.setFocusableInTouchMode(true);
//                currentExpense.setClickable(true);
//
//                newExpense.setEnabled(true);
//                newExpense.setFocusableInTouchMode(true);
//                newExpense.setClickable(true);
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }