package com.example.myapplication.Supplier;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class DisplaySupplierFragment extends Fragment {


    private List<String> supplierList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_supplier, container, false);

        supplierList = new ArrayList<>();
        supplierList.add("You?");
        supplierList.add("Yes,");
        supplierList.add("You!!");
        supplierList.add("Guess");
        supplierList.add("What??");
        supplierList.add("You");
        supplierList.add("Suck...");
        supplierList.add(":)");

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, supplierList);

        ListView listView = view.findViewById(R.id.supplier_display_listview);
        listView.setAdapter(mAdapter);

        return view;
    }
}