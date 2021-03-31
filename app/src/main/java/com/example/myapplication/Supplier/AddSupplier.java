package com.example.myapplication.Supplier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddSupplier extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private RegisterSupplierFragment registerSupplierFragment;
    private DisplaySupplierFragment displaySupplierFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);

        toolbar = findViewById(R.id.supplier_toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.supplier_viewpager);
        tabLayout = findViewById(R.id.supplier_tablayout);

        registerSupplierFragment = new RegisterSupplierFragment();
        displaySupplierFragment = new DisplaySupplierFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(registerSupplierFragment, "Register");
        viewPagerAdapter.addFragment(displaySupplierFragment, "Display");
        viewPager.setAdapter(viewPagerAdapter);

        // change this image in the tab bar for supplier
        tabLayout.getTabAt(0).setIcon(R.drawable.bottom_bg);
        tabLayout.getTabAt(1).setIcon(R.drawable.bottom_bg);

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}



/*
    Reference:
        Material Design Fragment - https://www.youtube.com/watch?v=eXK4VMI9XLI

 */