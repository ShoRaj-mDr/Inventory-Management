package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.warehousemanagmentsystem491b.R;

//import com.example.myapplication.R;

//import com.example.myapplication.R;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayCustomer extends AppCompatActivity {

    private ListView listView;
    private List<String> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer);

        listView = findViewById(R.id.customer_listview);
        customerList = new ArrayList<>();

        initializeList();

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
        listView.setAdapter(mAdapter);
    }

    public void initializeList() {
        customerList.add("Customer 1");
        customerList.add("Customer 2");
        customerList.add("Customer 3");
        customerList.add("Customer 4");
        customerList.add("Customer 5");
        customerList.add("Customer 6");
        customerList.add("Customer 7");
        customerList.add("Customer 8");
        customerList.add("Customer 9");
        customerList.add("Customer 10");
        customerList.add("Customer 11");
        customerList.add("Customer 12");
    }
}