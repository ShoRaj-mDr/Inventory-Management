package com.example.myapplication.ShoppingCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.amplify.generated.graphql.ListItemssQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.myapplication.ClientFactory;
import com.example.myapplication.Item.Item;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class AddItem extends AppCompatActivity {
    private int quantity, tempPrice, itemPosition;
    private String item, description, id, newPrice;
    private double price, totalPrice;
    private EditText quantityText, productPrice, productDescription, productId;
    private Button button;
    private Toolbar add_item_toolbar;

    private ArrayList<ListItemssQuery.Item> mItems;
    private ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
    private ArrayList<String> itemName = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private AutoCompleteTextView testName;

    private final GraphQLCall.Callback<ListItemssQuery.Data> queryCallback = new GraphQLCall.Callback<ListItemssQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListItemssQuery.Data> response) {
            mItems = new ArrayList<>(response.data().listItemss().items());
            System.out.println("TESTING PULLING ITEMS");
            System.out.println(mItems);
            for(int i = 0; i < mItems.size(); i++) {
                ArrayList<String> tempList = new ArrayList<String>();
                tempList.add(mItems.get(i).name());
                tempList.add(mItems.get(i).id());
                tempList.add(mItems.get(i).description());
                tempList.add(String.valueOf(mItems.get(i).price()));
                tempList.add(String.valueOf(mItems.get(i).quantity()));
                items.add(tempList);
            }

            itemName = new ArrayList<String>();
            for(int i = 0; i < items.size(); i++) {
                itemName.add(items.get(i).get(0));
            }
            System.out.println("TESTING IF ITEMS ARE HERE");
            System.out.println(itemName);
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            System.out.println("FAILURE");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        add_item_toolbar = findViewById(R.id.add_item_toolbar);
        setSupportActionBar(add_item_toolbar);

        add_item_toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        add_item_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ClientFactory.appSyncClient().query(ListItemssQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback);

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        quantityText = findViewById(R.id.add_item_product_quantity);
//        itemName = findViewById(R.id.add_item_product_name_edit_text);
        testName = findViewById(R.id.add_item_auto_complete);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemName);
        testName.setAdapter(adapter);
        productPrice = findViewById(R.id.add_item_product_price_edit_text);
        productDescription = findViewById(R.id.add_item_product_description_edit_text);
        productId = findViewById(R.id.add_item_product_id_edit_text);

        quantity = Integer.parseInt(quantityText.getText().toString());
        productPrice.setText("$0");

        testName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = testName.getText().toString();
                for(int x = 0; x < itemName.size(); x++) {
                    if(item.equals(itemName.get(x))) {
                        itemPosition = x;
                    }
                }
                description = items.get(itemPosition).get(2);
                id = items.get(itemPosition).get(1);
                productDescription.setText(description);
                productId.setText(id);
                price = Double.parseDouble(items.get(itemPosition).get(3));

                //Hides keyboard
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        quantityText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) ||
                        ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_TAB))) {
                    quantity = Integer.parseInt(quantityText.getText().toString());
                    newPrice = "$" + String.format("%.02f", (price * quantity));
                    productPrice.setText(newPrice);
                }
                return false;
            }
        });

        button = findViewById(R.id.add_item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPrice = String.format("%.02f", (price * quantity));
                totalPrice = Double.parseDouble(newPrice);
                if(totalPrice != 0 && item != null && quantity != 0) {
                    Item tempItem = new Item(item, totalPrice, quantity);
                    Intent intent = new Intent();
                    intent.putExtra("item", tempItem);
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
                System.out.println("FINISHED");

            }
        });
    }

    public void backToMenu(View view) {
        finish();
    }

    /**
     * Method that handles the onClick of decreasing the quantity
     */
    public void decreaseQuantity(View view) {
        quantity = Integer.parseInt(quantityText.getText().toString());
        quantity--;
        if(quantity >= 0) {
            quantityText.setText(String.valueOf(quantity));
            newPrice = "$" + String.format("%.02f", (price * quantity));
            productPrice.setText(newPrice);

            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Method that handles the onClick of increases the quantity
     */
    public void increaseQuantity(View view) {
        quantity = Integer.parseInt(quantityText.getText().toString());
        quantity++;
        if(quantity <= Integer.parseInt(items.get(itemPosition).get(4))) {
            quantityText.setText(String.valueOf(quantity));
            newPrice = "$" + String.format("%.02f", (price * quantity));
            productPrice.setText(newPrice);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } else {
            quantity--;
        }
    }
}