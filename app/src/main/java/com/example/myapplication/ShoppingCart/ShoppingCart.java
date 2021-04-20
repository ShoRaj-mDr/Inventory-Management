package com.example.myapplication.ShoppingCart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Checkout.Checkout;
import com.example.myapplication.Item.Item;
import com.example.myapplication.Orderlist.ItemAdapter;
import com.example.myapplication.Orderlist.SwipeToDeleteCallback;
import com.example.myapplication.R;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart extends AppCompatActivity {

    private final List<Item> items = new ArrayList<>(); //Make this static so Checkout can access
    private ShoppingCartAdapter mAdapter;
    private RecyclerView recyclerView;
    private int totalPrice = 0;
    private TextView textViewToChange;
    private Toolbar shopping_cart_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        shopping_cart_toolbar = findViewById(R.id.shopping_cart_toolbar);
        setSupportActionBar(shopping_cart_toolbar);

        shopping_cart_toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        shopping_cart_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textViewToChange = (TextView) findViewById(R.id.price);
        textViewToChange.setText("0");

        recyclerView = findViewById(R.id.rv_view);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new VegaLayoutManager());
        mAdapter = new ShoppingCartAdapter(items);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    /**
     * Create a new account when <- imageview is clicked
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    public void toCheckout(View view) {
        if (view.getId() == R.id.shopping_cart_button) {
            displayToast("Clicked checkout button");
            ArrayList<Item> stringArrayList = (ArrayList<Item>) items;
            System.out.println(stringArrayList);
            Intent checkout = new Intent(this, Checkout.class);
            checkout.putExtra("shoppingList", stringArrayList);
            startActivityForResult(checkout, 2);

        }
    }

    public void shoppingCartAdd(View view) {
        Intent addItem = new Intent(this, AddItem.class);
        startActivityForResult(addItem, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data.getParcelableExtra("item") != null) {
            Item item = data.getParcelableExtra("item");
            items.add(item);
            mAdapter.notifyDataSetChanged();
            double priceChange = Double.parseDouble(textViewToChange.getText().toString()) + item.getPrice();
            String newPrice = String.format("%.02f", priceChange);
            textViewToChange.setText(newPrice);
            displayToast("Item added!");
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
}