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
    private TextView modName, modPrice, modQuantity, oldQuantity;
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
            // Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.itemName.getText(), Toast.LENGTH_SHORT).show();
            // Toast.makeText(view.getContext(), "Item: " + this.itemName.getText(), Toast.LENGTH_SHORT).show();
            LayoutInflater modInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView = modInflater.inflate(R.layout.modify_shopping_cart_item, (ViewGroup) view.findViewById(R.id.mod_popup_layout));

            for(int i = 0; i < items.size(); i++) {
                if(this.itemName.getText().equals(items.get(i).getName())) {
                    pos = i;
                }
            }

            System.out.println(modifiedName + ", " + modifiedPrice + ", " + modifiedQuantity);

//            Display display = view.getWindowManager().getDefaultDisplay();
            WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            boolean focusable = true;
            popupWindow = new PopupWindow(popupView, (int)(width*.8), (int)(height*.4), focusable);
            popupWindow.setAnimationStyle(R.style.Animation);

            modName = popupView.findViewById(R.id.mod_shopping_cart_item_name);
            modPrice = popupView.findViewById(R.id.mod_shoppping_cart_item_price);
            modQuantity = popupView.findViewById(R.id.mod_real_quantity);
            quant = popupView.findViewById(R.id.mod_quantity);

            modName.setText(modifiedName);
            modPrice.setText(modifiedPrice);
            modQuantity.setText(modifiedQuantity);
            quant.setText(modifiedQuantity);

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            save = popupView.findViewById(R.id.mod_save_button);
            delete = popupView.findViewById(R.id.mod_delete_button);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newName = items.get(pos).getName();
                    newPrice = items.get(pos).getPrice();
                    newQuantity = items.get(pos).getQuantity();
                    newQuantity = Integer.parseInt(quant.getText().toString());

                    if(newQuantity > 0 && newQuantity <= newMaxQuantity) {
                        Item newItem = new Item(newName, newPrice, newQuantity, newMaxQuantity);

                        System.out.println("TEST: " + newName + ", " + newPrice + ", " + newQuantity);
                        items.set(pos, newItem);
                        System.out.println(items);
                        notifyItemChanged(pos);
                        //notifyDataSetChanged();
                        System.out.println("Updated item in shopping cart!");
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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // cheating way :p
                    ((ShoppingCart) mContext).setNewPrice(items.get(pos).getPrice());

                    items.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, items.size());
                    popupWindow.dismiss();
                    System.out.println("Removed item in the shopping cart!");
                }
            });

            ImageView inc = (ImageView) popupView.findViewById(R.id.mod_plus);
            ImageView dec = (ImageView) popupView.findViewById(R.id.mod_minus);

            inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newMaxQuantity = items.get(pos).getMaxQuantity();
                    newQuantity = Integer.parseInt(quant.getText().toString());
                    newQuantity++;
                    if(newQuantity <= newMaxQuantity) {
                        quant.setText(String.valueOf(newQuantity));
                        modQuantity.setText(String.valueOf(newQuantity));

                        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } else {
                        newQuantity--;
                    }
                }
            });

            dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newQuantity = Integer.parseInt(quant.getText().toString());
                    newQuantity--;
                    if(newQuantity >= 1) {
                        quant.setText(String.valueOf(newQuantity));
                        modQuantity.setText(String.valueOf(newQuantity));

                        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });

        }
    }


}
