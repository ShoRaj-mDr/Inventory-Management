package com.example.myapplication.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.ShoppingCart.ShoppingCart;
import com.example.myapplication.Item.Item;
import com.example.myapplication.CustomerMenu;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConfirmationFragment extends Fragment {

    TextView subtotalPrice,taxNum, totalNum;
    ArrayList<Item> shoppingCartList;
    Intent returnIntent;
    private int totalPrice = 0;
    private double taxPercentage = .0725;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation,container,false);
        Button back = (Button) view.findViewById(R.id.confirmation_to_payment);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Need to update quantity of items
                displayToast("Items are being processed and shipped");
                Intent mainMenu = new Intent( getActivity(), CustomerMenu.class);
                startActivity(mainMenu);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(1);
            }
        });

        shoppingCartList = new ArrayList<Item>();

//        ShoppingCart sc = new ShoppingCart();
//
//        ArrayList<String> shoppingCartList = new ArrayList<String>();
//        shoppingCartList = sc.getStringArrayList();

        returnIntent = new Intent();
        shoppingCartList = getActivity().getIntent().getExtras().getParcelableArrayList("shoppingList");

        ListView listView = (ListView) view.findViewById(R.id.confirm_list_view);

//        final ArrayAdapter<String> confirmAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,shoppingCartList);

        ConfirmListAdapter adapter = new ConfirmListAdapter(getActivity(), R.layout.list_confirmation, shoppingCartList);

        //Set adapter on list view
//        listView.setAdapter(confirmAdapter);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(null);

        for(int i = 0; i < shoppingCartList.size(); i++) {
            totalPrice += shoppingCartList.get(i).getPrice();
        }

        subtotalPrice = view.findViewById(R.id.subtotal_price);
        String subtotal = "$" + String.valueOf(totalPrice);
        subtotalPrice.setText(subtotal);

        taxNum = view.findViewById(R.id.tax_price);
        double temp = totalPrice * taxPercentage;
        BigDecimal bd = new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
        double taxCalc = bd.doubleValue();
        String tax = "$" + taxCalc;
        taxNum.setText(String.valueOf(tax));

        totalNum = view.findViewById(R.id.total_price);
        double totalCalc = taxCalc + totalPrice;
        String total = "$" + totalCalc;
        totalNum.setText(String.valueOf(total));



        return view;
    }

    /**
     * Displays a Toast with the message.
     *
     * @param message Message to display
     */
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message,
                Toast.LENGTH_SHORT).show();
    }
}