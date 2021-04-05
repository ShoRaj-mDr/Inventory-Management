package com.example.myapplication.DisplayCustomer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

//import com.example.warehousemanagmentsystem491b.R;
//import com.example.myapplication.R;
//import com.example.myapplication.R;

public class DisplayCustomer extends AppCompatActivity {

//    private ListView listView;

    private List<String> customerList;
    private RecyclerView recyclerView;
    private DisplayCustomerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer);

//        listView = findViewById(R.id.customer_listview);
        recyclerView = findViewById(R.id.customer_recyclerview);
        customerList = new ArrayList<>();

        initializeList();
//        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
//        listView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DisplayCustomerAdapter(customerList);
        recyclerView.setAdapter(mAdapter);

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

    /**
     * Displays a Toast with the message.
     *
     * @param message Message to display
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }


}