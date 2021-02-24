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
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import type.CreateItemzInput;
import type.CreatePetInput;

/*
    A class that ables the user to add and update individual items.

    When an item (category) is selected from the DBMainActivity, this DisplayContact activity
    is brought up. Thru this, the user is able to access the database table to add item type,
    Description about the item entered, the current expense (if item is already present in the
    table), and New Expense (constantly updating the table according to the user's choice).
 */
public class DisplayItems extends Activity {

    // Initialize the variables
   // private DBHelper mydb ;

    private TextView name ;
    private TextView descriptiontxt;
    private TextView currentExpense;
    private TextView newExpense;

    int p = 0;
    int id_To_Update = 0;
    int ourID;
    int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);

        name =  findViewById(R.id.editTextName);
        descriptiontxt =  findViewById(R.id.editTextPhone);
        currentExpense =  findViewById(R.id.editTextStreet);
        newExpense =  findViewById(R.id.editTextEmail);
        //mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        int exp=0;
        String name22 = "Something";
        String description22 = "Something else";
        ourID = getIntent().getIntExtra("ourID",0);
        itemID = getIntent().getIntExtra("itemid",0);
        name22=getIntent().getStringExtra("itemName");
        description22=getIntent().getStringExtra("itemDes");


        // get the correct item from the user's table through their id
        if(extras !=null) {
            int Value = extras.getInt("id");

            // using a cursor to navigate around the table
//            if(Value > 0){
//                Cursor rs = mydb.getDataExpensesForItem(ourID,itemID);
//                Cursor sr = mydb.getDataItem(ourID,itemID);
//                id_To_Update = Value;

                //Get the expense for an item.
//                if(rs.getCount()>0) {
//                    rs.moveToFirst();
//                    while (!rs.isAfterLast()) {
//                        exp = rs.getInt(rs.getColumnIndex(DBHelper.EXPENSES_COLUMN_PRICE));
//                        Toast.makeText(getApplicationContext(), "EXP: "+ exp,
//                                Toast.LENGTH_SHORT).show();
//                        rs.moveToNext();
//                    }
//                }

                //Get the item name and description
//                if(sr.getCount() > 0) {
//                    sr.moveToFirst();
//                    while (!sr.isAfterLast()) {
//                        name22 = sr.getString(sr.getColumnIndex(DBHelper.ITEMS_COLUMN_ITEM));
//                        description22 = sr.getString(sr.getColumnIndex(DBHelper.ITEMS_COLUMN_DESCRIPTION));
//                        Toast.makeText(getApplicationContext(), "EXP: "+exp,
//                                Toast.LENGTH_SHORT).show();
//                        sr.moveToNext();
//                    }
//                }

                String strStreet="";

//                if (!rs.isClosed())  {
//                    rs.close();
//                }
                Button b = (Button)findViewById(R.id.button1);
                name.setText(name22);
                name.setFocusable(true);
                name.setClickable(true);

                descriptiontxt.setText(description22);
                descriptiontxt.setEnabled(true);
                descriptiontxt.setFocusableInTouchMode(true);
                descriptiontxt.setClickable(true);

                currentExpense.setText(String.valueOf(exp));
                currentExpense.setFocusable(false);
                currentExpense.setClickable(false);

                newExpense.setText((CharSequence) strStreet);
                newExpense.setEnabled(true);
                newExpense.setFocusableInTouchMode(true);
                newExpense.setClickable(true);

            }
        }
  //  }

    /**
     *  This is a action-bar menu on top
     *
     * @param menu
     * @return boolean true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getMenuInflater().inflate(R.menu.display_contact, menu);
            } else{
                getMenuInflater().inflate(R.menu.dbmain_menu, menu);
            }
        }
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                Button c = findViewById(R.id.button);

                // feature: enables or disables when required
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        p = 1;
                    }
                });
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                descriptiontxt.setEnabled(true);
                descriptiontxt.setFocusableInTouchMode(true);
                descriptiontxt.setClickable(true);

                currentExpense.setEnabled(true);
                currentExpense.setFocusableInTouchMode(true);
                currentExpense.setClickable(true);

                newExpense.setEnabled(true);
                newExpense.setFocusableInTouchMode(true);
                newExpense.setClickable(true);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * To delete the item from the list
     *
     * @param view
     */
    public void run2(View view) { //DELETE ITEM
//        Bundle extras = getIntent().getExtras();
//        if(extras !=null) {
//            String s = mydb.getOneItemName(ourID, itemID);
//            float d = mydb.getAllExpensesForOneDayAndOneItem(ourID, s, date());
//            //if d >0 then the item we are trying to delete already has a monetary value.
//            if (!(d > 0)) {
//                int Value = extras.getInt("id");
//                if (Value > 0) {
//                    mydb.deleteItem(itemID);
//                    Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), DBMainActivity.class);
//                    intent.putExtra("itemid", ourID);
//                    startActivity(intent);
//                }
//            }
//            else{
//                Toast.makeText(getApplicationContext(), "Could not be deleted\nContains value: $"+d, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), DBMainActivity.class);
//                intent.putExtra("itemid", ourID);
//                startActivity(intent);
//
//            }
//        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int Value = extras.getInt("id");
            save();
            if(Value>0){
                String currentDate = date();
                //Update DB.. adding an item
//                if(mydb.updateExpense(itemID,ourID,itemID,Integer.parseInt(newExpense.getText().toString()),currentDate)){
                    Toast.makeText(getApplicationContext(), "Updated",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), myItems.class);
                    intent.putExtra("itemid",ourID);
//                    int ourBudget=mydb.getMaxExpense(ourID);
//                    int o = mydb.getSumDaily(ourID,0, date());

//                    if(!mydb.updateDailySavings(ourID,o,ourBudget)){
//                        Toast.makeText(getApplicationContext(), "Our budget has been surpassed ",
//                                Toast.LENGTH_SHORT).show();
//                    }

                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                save();
                String currentDate=date();
                // when value =0 we are adding a completly new item
//                if(mydb.insertItem(0,ourID,name.getText().toString(), descriptiontxt.getText().toString(),
//                        Integer.parseInt(newExpense.getText().toString()),currentDate)){
//
//                    Toast.makeText(getApplicationContext(), "done",
//                            Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "not done",
//                            Toast.LENGTH_SHORT).show();
//                }

                Intent intent = new Intent(getApplicationContext(), myItems.class);
                intent.putExtra("itemid",ourID);
//                int ourBudget=mydb.getMaxExpense(ourID);
//                int o = mydb.getSumDaily(ourID,0, date());

//                if(!mydb.updateDailySavings(ourID,o,ourBudget)){
//                    Toast.makeText(getApplicationContext(), "Our budget has been surpassed ", Toast.LENGTH_SHORT).show();
//                }
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
                    Toast.makeText(DisplayItems.this, "Failed to add pet", Toast.LENGTH_SHORT).show();
                    DisplayItems.this.finish();
                }
            });
        }
    };

}