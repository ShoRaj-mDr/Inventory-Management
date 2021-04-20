package com.example.myapplication.Checkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.example.myapplication.Checkout.ui.main.SectionsPagerAdapter;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

public class Checkout extends AppCompatActivity {

    private Toolbar checkout_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkout_toolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(checkout_toolbar);

        checkout_toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_button_24);
        checkout_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        LinearLayout tabStrip = ((LinearLayout)tabs.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
    }

    /**
     * Create a new account when <- imageview is clicked
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }
}