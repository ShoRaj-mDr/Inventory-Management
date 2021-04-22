package com.example.myapplication.ShoppingCart;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Item.Item;
import com.example.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private final ArrayList<Item> items;
    private String modifiedName, modifiedPrice, modifiedQuantity, newName;
    private TextView modName, modPrice, modQuantity;
    PopupWindow popupWindow;
    View popupView;
    Button save, delete;
    Item item;
    private int pos, newQuantity, newMaxQuantity;
    private double newPrice;
    EditText quant;

    private Context mContext;

    public ShoppingCartAdapter(ArrayList<Item> itemList, Context mContext) {
        this.items = itemList;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public ShoppingCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_shopping_cart, parent, false);
        mContext = parent.getContext();
        return new ShoppingCartAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ShoppingCartAdapter.ViewHolder holder, int position) {
        item = items.get(position);
        modifiedName = item.getName();
        modifiedPrice = String.valueOf(item.getPrice());
        modifiedQuantity = String.valueOf(item.getQuantity());
//        if (item.getName() != null) {
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.itemPrice.setText("$ " + item.getPrice());
//        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemName = itemView.findViewById(R.id.itemView);
            itemQuantity = itemView.findViewById(R.id.quantityView);
            itemPrice = itemView.findViewById(R.id.priceView);
        }

        @Override
        public void onClick(View view) {
            LayoutInflater modInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView = modInflater.inflate(R.layout.modify_shopping_cart_item, (ViewGroup) view.findViewById(R.id.mod_popup_layout));

            // Initializes the clicked item's information
            // name, price, quantity, and position
            for(int i = 0; i < items.size(); i++) {
                if(this.itemName.getText().equals(items.get(i).getName())) {
                    pos = i;
                    newName = items.get(pos).getName();
                    newQuantity = items.get(pos).getQuantity();
                    newPrice = items.get(pos).getPrice();
                }
            }

            System.out.println(modifiedName + ", " + modifiedPrice + ", " + modifiedQuantity);

            // Sets up the popup window
            WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            boolean focusable = true;
            popupWindow = new PopupWindow(popupView, (int)(width*.8), (int)(height*.4), focusable);
            popupWindow.setAnimationStyle(R.style.Animation);

            // initializes the textviews
            modName = popupView.findViewById(R.id.mod_shopping_cart_item_name);
            modPrice = popupView.findViewById(R.id.mod_shoppping_cart_item_price);
            modQuantity = popupView.findViewById(R.id.mod_real_quantity);
            quant = popupView.findViewById(R.id.mod_quantity);

            // Sets strings to match the item's information
            modName.setText(newName);
            modPrice.setText(String.valueOf(newPrice));
            modQuantity.setText(String.valueOf(newQuantity));
            quant.setText(String.valueOf(newQuantity));

            // Makes the popup window appear in the screen
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            // Initializes both button to set up when they are clicked
            save = popupView.findViewById(R.id.mod_save_button);
            delete = popupView.findViewById(R.id.mod_delete_button);

            save.setOnClickListener(new View.OnClickListener() {    // When save button is clicked
                @Override
                public void onClick(View view) {
                    newQuantity = Integer.parseInt(quant.getText().toString());

                    if(newQuantity > 0 && newQuantity <= newMaxQuantity) {
                        // Formats the price to 2 decimal points
                        String correctFormat = String.format("%.02f", newPrice);
                        newPrice = Double.parseDouble(correctFormat);
                        // Creates new item to replace old item
                        Item newItem = new Item(newName, newPrice, newQuantity, newMaxQuantity);

                        System.out.println("TEST: " + newName + ", " + newPrice + ", " + newQuantity);
                        //Replaces item in the arraylist
                        items.set(pos, newItem);
                        System.out.println(items);
                        //Notify that an item is replaced
                        notifyItemChanged(pos);
                        ((ShoppingCart) mContext).changingPrice();
                        System.out.println("Updated item in shopping cart!");
                        // Closes popupWindow
                        popupWindow.dismiss();
                    } else if (newQuantity > newMaxQuantity) {
                        quant.setError("Number inputted exceeds available quantity!");
                    } else if (newQuantity == 0) {
                        quant.setError("Number cannot be 0 otherwise just delete item!");
                    } else {
                        quant.setError("Invalid quantity!");
                    }


                }
            });

            delete.setOnClickListener(new View.OnClickListener() { // When delete button is clicked
                @Override
                public void onClick(View view) {
                    // cheating way :p
                    ((ShoppingCart) mContext).setNewPrice(items.get(pos).getPrice());
                    // Removes Item in the arraylist
                    items.remove(pos);
                    // Notifies the recycler view that the arraylist changed
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, items.size());
                    // Closes popup window
                    popupWindow.dismiss();
                    System.out.println("Removed item in the shopping cart!");
                }
            });

            // Initializes both image views to set up when they are clicked
            ImageView inc = (ImageView) popupView.findViewById(R.id.mod_plus);
            ImageView dec = (ImageView) popupView.findViewById(R.id.mod_minus);

            inc.setOnClickListener(new View.OnClickListener() { // When plus button is clicked
                @Override
                public void onClick(View view) {
                    newMaxQuantity = items.get(pos).getMaxQuantity();
                    newQuantity = Integer.parseInt(quant.getText().toString());
                    newQuantity++; // This is to change the Quality to match what the user does
                    if(newQuantity <= newMaxQuantity) {
                        // Getting individual price and getting the new price by
                        // individual price * quantity
                        double p = Double.parseDouble(modPrice.getText().toString());
                        double q = Double.parseDouble(modQuantity.getText().toString());
                        double singleItemPrice = p / q;
                        newPrice = singleItemPrice * newQuantity;
                        // Formats the price to 2 decimal points
                        String correctFormat = String.format("%.02f", newPrice);
                        newPrice = Double.parseDouble(correctFormat);

                        // Changes textviews to represent the present
                        quant.setText(String.valueOf(newQuantity));
                        modQuantity.setText(String.valueOf(newQuantity));
                        modPrice.setText(String.valueOf(newPrice));

                        // Makes sure that if a keyboard is open, it closes it
                        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } else {
                        newQuantity--;
                    }
                }
            });

            dec.setOnClickListener(new View.OnClickListener() {     // When minus button is clicked
                @Override
                public void onClick(View view) {
                    newQuantity = Integer.parseInt(quant.getText().toString());
                    newQuantity--;
                    if(newQuantity >= 1) {
                        // Getting individual price and getting the new price by
                        // individual price * quantity
                        double p = Double.parseDouble(modPrice.getText().toString());
                        double q = Double.parseDouble(modQuantity.getText().toString());
                        double singleItemPrice = p / q;
                        newPrice = singleItemPrice * newQuantity;
                        // Formats the price to 2 decimal points
                        String correctFormat = String.format("%.02f", newPrice);
                        newPrice = Double.parseDouble(correctFormat);

                        // Changes textviews to represent the present
                        quant.setText(String.valueOf(newQuantity));
                        modQuantity.setText(String.valueOf(newQuantity));
                        modPrice.setText(String.valueOf(newPrice));

                        // Makes sure that if a keyboard is open, it closes it
                        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });

        }
    }


}
