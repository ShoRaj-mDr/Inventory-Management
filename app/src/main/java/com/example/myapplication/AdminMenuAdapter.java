package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminMenuAdapter extends BaseAdapter {
    Context context;
    String[] employeeMenu;
    int[] employeeMenuImage;
    LayoutInflater layoutInflater;
    public AdminMenuAdapter(Context applicationContext, String[] employeeMenu, int[] employeeMenuImage) {
        this.context = context;
        this.employeeMenu = employeeMenu;
        this.employeeMenuImage = employeeMenuImage;
        this.layoutInflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return employeeMenu.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.activity_admin_menu_adapter, null);
        TextView mainmenuText = view.findViewById(R.id.employee_mainmenu_textView);
        ImageView mainmenuImage = view.findViewById(R.id.employee_mainmenu_imageView);
        ;
        mainmenuText.setText(employeeMenu[i]);
        mainmenuImage.setImageResource(employeeMenuImage[i]);
        return view;
    }
}
