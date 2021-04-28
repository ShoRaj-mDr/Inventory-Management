package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.amplify.generated.graphql.CreateShiftMutation;
import com.amazonaws.amplify.generated.graphql.DeleteShiftMutation;
import com.amazonaws.amplify.generated.graphql.ListShiftsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateShiftInput;
import type.DeleteShiftInput;

public class shiftActivity extends AppCompatActivity {

    String itemIDglobal;
    private ArrayList<ListShiftsQuery.Item> mShifts;
    private final String TAG = myItems.class.getSimpleName();
    private ListView listViewObj;

    CalendarView calender;
    TextView date_viewHere;
    TimePicker timeStart;
    TimePicker timeEnd;

    TextView startView;
    TextView endView;

    boolean clickedRemove;
    boolean clickedEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);
/*
        Button removeBtn = (Button) findViewById(R.id.removeShiftButton);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayToast("listener remove");
            }
        });

        Button editBtn = (Button) findViewById(R.id.editShiftButton);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayToast("edit remove");
            }
        });

        // get the data from the textboxes
 */
        calender = (CalendarView)
                findViewById(R.id.calender);  //calender from xml
        date_viewHere = (TextView)
                findViewById(R.id.date_view); //date view from xml

        calender
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                String Date
                                        = (month + 1)  + "-"
                                        + dayOfMonth + "-" + year;

                                // set this date in TextView for Display
                                date_viewHere.setText(Date);
                                displayToast(Date);
                            }
                        });

        timeStart = (TimePicker)
                findViewById(R.id.timePicker1);
        startView = (TextView)
                findViewById(R.id.timeStart_view);

        timeStart
                .setOnTimeChangedListener(
                        new OnTimeChangedListener() {
                            @Override
                            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int min) {
                                String timeString = hourOfDay+":"+min;

                                startView.setText(timeString);
                                //displayToast(timeString);

                            }
                        }


                );

        timeEnd = (TimePicker)
                findViewById(R.id.timePicker2);
        endView = (TextView)
                findViewById(R.id.timeEnd_view);

        timeEnd
                .setOnTimeChangedListener(
                        new OnTimeChangedListener() {
                            @Override
                            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int min) {
                                String timeString2 = hourOfDay+":"+min;

                                endView.setText(timeString2);
                                //displayToast(timeString2);

                            }
                        }
                );

        query();




    }


    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }


    //-------------------------------------------------------------------------- ADD
    public void pressedSave(View view) {
        displayToast("pressed save");
        addItem();
    }

    private void addItem() {
        //final string date = (TextView)findViewById(R.id.date_view).getText();   //error??
        //TextView date = (TextView)findViewById(R.id.date_view);
        //final String dateString = (String) date.getText();
        //displayToast("final date:" + date.getText());

        final String date_string = ((TextView) findViewById(R.id.date_view)).getText().toString();
        final String time_start = ((TextView) findViewById(R.id.timeStart_view)).getText().toString();
        final String time_end = ((TextView) findViewById(R.id.timeEnd_view)).getText().toString();

        final String empName = ((EditText) findViewById(R.id.employeeTextView)).getText().toString();
        displayToast("final date:" + date_string + " " + time_start + " " + time_end);

        /*
        input CreateShiftInput {
              id: ID
              employeeID: Int
              date: String
              start: String
              end: String
            }
         */

        //        CreateShiftInput input = CreateShiftInput.builder()
        //                .employeeID( ... )
        //                .date(date_String)  -> column not updated...
        //                .start(time_start)
        //                .end(time_end)
        //                .build();

        CreateShiftInput input = CreateShiftInput.builder().start(empName).end(date_string+" , "+ time_start+"-"+time_end).build();
        CreateShiftMutation addShiftMutation = CreateShiftMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(addShiftMutation).enqueue(mutateCallback001);


        ((EditText)  findViewById(R.id.employeeTextView)).setText("");

    }

    private GraphQLCall.Callback<CreateShiftMutation.Data> mutateCallback001 = new GraphQLCall.Callback<CreateShiftMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateShiftMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(shiftActivity.this, "Added Item", Toast.LENGTH_SHORT).show();
                    //shiftActivity.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    Toast.makeText(shiftActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    //shiftActivity.this.finish();
                }
            });
        }
    };
    //-------------------------------------------------------------------------- ADD


    //-------------------------------------------------------------------------- REMOVE
    public void pressedRemove(View view) {
        displayToast("pressed remove");
        delete(itemIDglobal);
    }


    //-------------------------------------------------------------------------- REMOVE

    public void pressedEdit(View view) {
        displayToast("pressed edit");
        addItem();
    }

    //-------------------------------------------------------------------------- DISPLAY
    public void pressedRefresh(View view) {
        displayToast("pressed refresh");
        query();
/*
        listViewObj = findViewById(R.id.shift_list);
        listViewObj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String itemID = mShifts.get(position).id();
                displayToast(itemID);

                //Intent intent = new Intent(getApplicationContext(), DisplayItems.class);


                //startActivity(intent);
            }
        });


 */
        // myFunc2(mShifts);
    }

    public void query() {
        ClientFactory.appSyncClient().query(ListShiftsQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback001);
    }

    private GraphQLCall.Callback<ListShiftsQuery.Data> queryCallback001 = new GraphQLCall.Callback<ListShiftsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListShiftsQuery.Data> response) {

            //mPets = new ArrayList<>(response.data().listItemzs().items());


            mShifts = new ArrayList<>(response.data().listShifts().items());


            Log.i(TAG, "Retrieved list items: " + mShifts.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    myFunc2(mShifts);

                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

    public void myFunc(List<ListShiftsQuery.Item> items) {

        //This loop will concatenate all our item names with their associated price.
        final ArrayList lastAL = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            lastAL.add(i, items.get(i).start() + "    " + items.get(i).end());
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lastAL);

        listViewObj = findViewById(R.id.shift_list);
        listViewObj.setAdapter(arrayAdapter);
        listViewObj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Bundle dataBundle = new Bundle();
                String itemID=mShifts.get(position).id();
                dataBundle.putInt("id", 0);

                /*
                Intent intent = new Intent(getApplicationContext(), DisplayItems.class);
                intent.putExtra("itemID",mItems.get(position).id());
                intent.putExtra("itemName",mItems.get(position).name());
                intent.putExtra("itemDes",mItems.get(position).description());
                intent.putExtra("itemPrice",mItems.get(position).price());
                intent.putExtra("itemQuantity",mItems.get(position).quantity());

                intent.putExtras(dataBundle);
                startActivity(intent);
                */

            }
        });
    }


    //-------------------------------------------------------------------------- DISPLAY



    public void myFunc2(List<ListShiftsQuery.Item> items) {

        //This loop will concatenate all our item names with their associated price.
        final ArrayList lastAL = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            lastAL.add(i, items.get(i).start() + "    " + items.get(i).end());
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lastAL);

        listViewObj = findViewById(R.id.shift_list);
        listViewObj.setAdapter(arrayAdapter);
        listViewObj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Bundle dataBundle = new Bundle();
                String itemID = mShifts.get(position).id();
                //---------
                displayToast(itemID);
                //---------
                //delete(itemID);
                itemIDglobal = itemID;

            }
        });
    }

    private void delete(String id) {
        DeleteShiftInput input =DeleteShiftInput.builder().id(id).build();

        DeleteShiftMutation deleteShiftMutation= DeleteShiftMutation.builder().input(input).build();
        ClientFactory.appSyncClient().mutate(deleteShiftMutation).enqueue(mutateCallback002);
    }

    // Mutation callback code
    private GraphQLCall.Callback<DeleteShiftMutation.Data> mutateCallback002 = new GraphQLCall.Callback<DeleteShiftMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<DeleteShiftMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(shiftActivity.this, "Deleted Item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform DeleteItemMutation", e);
                    Toast.makeText(shiftActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    //DisplayItems.this.finish();
                }
            });
        }
    };




}