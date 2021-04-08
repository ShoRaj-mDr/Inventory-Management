package com.example.myapplication.Orderlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Item.Item;
import com.example.myapplication.R;

public class CreateItem extends AppCompatActivity {

    // Member variables for creating a new item
    private EditText itemName;
    private EditText itemID;
    private EditText itemPrice;
    private EditText itemDescr;
    private EditText itemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        itemName = findViewById(R.id.orderlist_item_name_txt);
        itemID = findViewById(R.id.orderlist_item_id_txt);
        itemPrice = findViewById(R.id.orderlist_item_price_txt);
        itemDescr = findViewById(R.id.orderlist_item_descr_txt);
        itemQuantity = findViewById(R.id.orderlist_item_quantity_txt);

        initialValueTextField(itemName, "Ex: Item Name");
        initialValueTextField(itemID, "Ex: 1101");
        initialValueTextField(itemPrice, "Ex: 29.99");
        initialValueTextField(itemDescr, "Ex: Item Description");

        // catch error
        Button button = findViewById(R.id.orderlist_addProductButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int id = Integer.parseInt(itemID.getText().toString());     // get an error in this
                int id = 0;

                try {       // make this so can catch any error for all
                    id = Integer.parseInt(itemID.getText().toString());
                } catch (NumberFormatException nfe) {
                    // Handle the condition when str is not a number.
                    Log.i("Error in CreateItem ", " Parsing String to Integer Error");
                }

                String name = itemName.getText().toString();
                String descr = itemDescr.getText().toString();
                double price = Double.parseDouble(itemPrice.getText().toString());
                int quantity = Integer.parseInt(itemQuantity.getText().toString());

                Item item = new Item(id, name, descr, price, quantity);

                Intent intent = new Intent();
                intent.putExtra("item", item);
                setResult(RESULT_OK, intent);
                finish();       // finishing activity
            }
        });

    }

    public void initialValueTextField(final EditText editText, final String text) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    editText.setHint(text);
                else
                    editText.setHint("");
            }
        });
    }

    /**
     * Method that handles the onClick of decreasing the quantity
     */
    public void decreaseQuantity(View view) {
        int quantity = Integer.parseInt(itemQuantity.getText().toString());
        if (quantity < 0)
            quantity = 0;
        else
            quantity--;
        itemQuantity.setText(String.valueOf(quantity));
    }

    /**
     * Method that handles the onClick of increases the quantity
     */
    public void increaseQuantity(View view) {
        int quantity = Integer.parseInt(itemQuantity.getText().toString());
        quantity++;
        itemQuantity.setText(String.valueOf(quantity));
    }
}