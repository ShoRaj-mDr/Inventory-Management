package com.example.myapplication.Orderlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Item.Item;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class OrderList extends AppCompatActivity {

    private final List<Item> items = new ArrayList<>();
    private ItemAdapter mAdapter;
    private RecyclerView recyclerView;

    // Initializing Fab variables
    private TextView addProductTextView;
    private FloatingActionButton fab1_main, fab2_addProduct;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private Boolean fab1_main_isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        addProductTextView = findViewById(R.id.orderlist_addProductText);
        fab1_main = findViewById(R.id.orderlist_FAB1);
        fab2_addProduct = findViewById(R.id.orderlist_FAB2);

        recyclerView = findViewById(R.id.rv_itemDisplay);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new VegaLayoutManager());
        mAdapter = new ItemAdapter(items);
        new ItemTouchHelper(new SwipeToDeleteCallback(mAdapter)).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        /*
        items.add(new Item(101, "Coke", "Coke", 49.99, 40));
        items.add(new Item(102, "Cheese", "Coke", 67.23, 25));
        items.add(new Item(103, "Bread", "Coke", 12.59, 65));
        items.add(new Item(104, "Egg", "Coke", 19.99, 79));
        items.add(new Item(105, "Ham", "Coke", 45.65, 48));
        items.add(new Item(106, "Spinach", "Coke", 48.65, 70));
        items.add(new Item(107, "Garlic", "Coke", 78.99, 56));
        items.add(new Item(108, "Weed", "Coke", 1.99, 555));
        items.add(new Item(109, "Raspberry", "Coke", 98.49, 5));
        items.add(new Item(110, "Apple", "Coke", 150.46, 1));
        // animateFab();
         */


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult");
        if (resultCode == RESULT_OK && data.getParcelableExtra("item") != null) {
            Log.d("TAG", "onActivityResult: OK");
            Item item = data.getParcelableExtra("item");
            items.add(item);
            mAdapter.notifyDataSetChanged();
            displayToast("Item added!");
        } else {
            Log.d("TAG", "onActivityResult: ERROR");
        }
    }


    /**
     * Close the main FAB menu
     */
    public void closeFabMenu() {
        addProductTextView.setVisibility(View.INVISIBLE);
        fab2_addProduct.startAnimation(fab_close);
        fab1_main.startAnimation(fab_close);
        fab1_main.startAnimation(fab_anticlock);
        fab2_addProduct.setClickable(false);
        fab1_main_isOpen = false;
    }

    /*
    public void animateFab() {
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);
    }
     */

    public void fab1_main_onClick(View view) {
        Intent intentOrderItem = new Intent(this, CreateItem.class);
        startActivityForResult(intentOrderItem, 1);

        /* enable this for fab animation
        if (fab1_main_isOpen) {
            closeFabMenu();
        } else {
            addProductTextView.setVisibility(View.VISIBLE);
            fab2_addProduct.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1_main.startAnimation(fab_open);
            fab1_main.startAnimation(fab_clock);
            fab2_addProduct.setClickable(true);
            fab3.setClickable(true);
            fab1_main_isOpen = true;
        } */
    }

    public void fab2_addProduct_onClick(View view) {
        closeFabMenu();             // close the FAB menu before going to the next activity
        Intent intentOrderItem = new Intent(this, CreateItem.class);
        startActivity(intentOrderItem);
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


/*
    Resources used:
        RecyclerView: https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
        Search bar: https://www.journaldev.com/12478/android-searchview-example-tutorial
        FAB tool: https://medium.com/@shubham_nikam/easy-way-to-add-minimal-expandable-floating-action-button-fab-menu-dd8e6e011f52 -> (Used)
                                    OR
                  https://github.com/nambicompany/expandable-fab


    RecyclerView Designs:
        https://github.com/jiang111/Awesome-RecyclerView-LayoutManager
 */
