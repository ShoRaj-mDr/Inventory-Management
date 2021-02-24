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
    // private DBHelper mydb;

    private TextView expenseMain;
    private TextView savingsGoal;
    private TextView nameMain;
    private TextView txtDailySavings;
    int ourID;
    int xx;

    private ArrayList<ListItemzsQuery.Item> mPets;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);
        expenseMain = findViewById(R.id.textView);
        savingsGoal = findViewById(R.id.txtSavingsGoal);
        nameMain = findViewById(R.id.textView2);
        txtDailySavings = findViewById(R.id.txtDailySavings);
        ClientFactory.init(this);

        //mydb = new DBHelper(this);
        int x = 0;

        // Retrieve the username from the MainActivity

//        Intent intent = getIntent();
//        String message = intent.getStringExtra("first message");
//        ourID = intent.getIntExtra("itemid",0);

        // Update the text on the welcome screen
//        String todayDate = date();
//        int o = mydb.getSumDaily(ourID,0, todayDate);

        // show the user's name
//        String ourName = mydb.getOurName(ourID);
//        nameMain.setText(nameMain.getText().toString() + ourName);
//
//        String ourGoal = mydb.getOurIncome(ourID);
//        String ourGoal2 = mydb.getOurIncome(ourID);

        //Pulling values from the DB to fill in the main menu summary.
//        String myIncome = mydb.getOurGoal(ourID);
//        Double dailyExp = (Double.valueOf(myIncome)/365) - o;
//        Double d1=Double.valueOf(ourGoal);
//        Double d2=Double.valueOf(o);
//        Double dailyExp2=(d1-d2);
//        String result = currencyFormat(String.valueOf(Double.toString(dailyExp)));
//        String todaysExpense = currencyFormat(Integer.toString(o));
//        expenseMain.setText(expenseMain.getText().toString() + "$" + todaysExpense);
//
//        ourGoal = currencyFormat(ourGoal);
//        savingsGoal.setText(savingsGoal.getText().toString() + " $" + ourGoal2);
//
//        String result2 = currencyFormat(String.valueOf(Double.toString(dailyExp2)));
//        txtDailySavings.setText(txtDailySavings.getText().toString() + " $" + result2);

        //String dummyDate="2020-07-2";
//        final ArrayList array_list = mydb.getAllItemsNameWithDate(ourID, todayDate);
//        ArrayList array_list2 = new ArrayList();
//        final ArrayList lastAL = new ArrayList();
//
//        //This loop will concatenate all our item names with their associated price.
//        for (int i = 0; i < array_list.size(); i++) {
//            float here = mydb.getAllExpensesForOneDayAndOneItem(ourID, array_list.get(i).toString(), todayDate);
//            String formating = currencyFormat(String.valueOf(here));
//            array_list2.add(i, formating);
//            lastAL.add(i, array_list.get(i).toString() + "    $" + array_list2.get(i));
//        }
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lastAL);
//        obj = findViewById(R.id.listView1);
//        obj.setAdapter(arrayAdapter);
//        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//                int id_To_Search = position + 1;
//
//                Bundle dataBundle = new Bundle();
//                dataBundle.putInt("id", id_To_Search);
//
//                Intent intent = new Intent(getApplicationContext(), DisplayItems.class);
//                intent.putExtra("ourID", ourID);
////                Toast.makeText(getApplicationContext(), array_list.get(id_To_Search-1).toString(),
////                        Toast.LENGTH_SHORT).show();
//
//                Cursor u = mydb.getItemID(ourID, array_list.get(id_To_Search - 1).toString());
//                if (u.getCount() > 0) {
//                    u.moveToFirst();
//                    while (!u.isAfterLast()) {
//                        xx = u.getInt(u.getColumnIndex(DBHelper.ITEMS_COLUMN_ID));
//                        u.moveToNext();
//                    }
//                }
//
//                intent.putExtra("itemid", xx);
//                intent.putExtras(dataBundle);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();

        // Query list data when we return to the screen
        query();
    }

    public void query() {
        ClientFactory.appSyncClient().query(ListItemzsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);
    }

    private GraphQLCall.Callback<ListItemzsQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemzsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemzsQuery.Data> response) {

            mPets = new ArrayList<>(response.data().listItemzs().items());

            Log.i(TAG, "Retrieved list items: " + mPets.toString());
            //myFunc(mPets);



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mAdapter.setItems(mPets);
                    //mAdapter.notifyDataSetChanged();

                    myFunc(mPets);

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
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getApplicationContext(), DisplayItems.class);
                intent.putExtras(dataBundle);
                intent.putExtra("ourID", ourID);
                intent.putExtra("itemid", xx);
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


    public void myFunc(List<ListItemzsQuery.Item> items) {
        //final ArrayList lastAL = new ArrayList();
        List<ListItemzsQuery.Item> items2=new ArrayList<>();
        //items2=items;

                //This loop will concatenate all our item names with their associated price.
     final ArrayList lastAL = new ArrayList();
        for (int i = 0; i < items.size(); i++) {

            lastAL.add(i, items.get(i).name() + "    " + items.get(i).description());
        }




        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lastAL);

        obj = findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                int id_To_Search = position + 1;

                Bundle dataBundle = new Bundle();
                String itemID=mPets.get(position+0).id();
                dataBundle.putString("id", itemID);

                Intent intent = new Intent(getApplicationContext(), DisplayItems.class);
                intent.putExtra("ourID", ourID);
                intent.putExtra("itemName",mPets.get(position).name());
                intent.putExtra("itemDes",mPets.get(position).description());
//                Toast.makeText(getApplicationContext(), array_list.get(id_To_Search-1).toString(),
//                        Toast.LENGTH_SHORT).show();

//                Cursor u = mydb.getItemID(ourID, array_list.get(id_To_Search - 1).toString());
//                if (u.getCount() > 0) {
//                    u.moveToFirst();
//                    while (!u.isAfterLast()) {
//                        xx = u.getInt(u.getColumnIndex(DBHelper.ITEMS_COLUMN_ID));
//                        u.moveToNext();
//                    }
//                }

                //intent.putExtra("itemid", xx);
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

    public String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(dNow);
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }
}