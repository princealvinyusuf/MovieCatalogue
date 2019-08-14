package com.intent.moviecatalogue.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerTab extends FragmentPagerAdapter {
    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listID = new ArrayList<>();

    public ViewPagerTab(FragmentManager fm) {
        super(fm);
    }

    public void AddFragment(Fragment fragment, String title) {
        listFragment.add(fragment);
        listID.add(title);
    }

    @Override
    public Fragment getItem(int i) {
        return listFragment.get(i);
    }

    @Override
    public int getCount() {
        return listID.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listID.get(position);
    }

}