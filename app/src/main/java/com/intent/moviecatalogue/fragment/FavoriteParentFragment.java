package com.intent.moviecatalogue.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.adapter.ViewPagerTab;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteParentFragment extends Fragment {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public FavoriteParentFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_parent, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState){
        super.onViewCreated(view,saveInstanceState);

        ButterKnife.bind(this,view);

        ViewPagerTab viewPagerTab = new ViewPagerTab(getChildFragmentManager());

        viewPagerTab.AddFragment(new FavoriteMovieFragment(),getString(R.string.movies));
        viewPagerTab.AddFragment(new FavoriteTvFragment(),getString(R.string.tv_show));

        viewPager.setAdapter(viewPagerTab);
        tabLayout.setupWithViewPager(viewPager);
    }

}
