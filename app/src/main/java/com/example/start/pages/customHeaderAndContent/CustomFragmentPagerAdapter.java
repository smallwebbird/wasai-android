package com.example.start.pages.customHeaderAndContent;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class CustomFragmentPagerAdapter<T> extends FragmentPagerAdapter {
    private List<T> fragments;

    public CustomFragmentPagerAdapter(@NonNull FragmentManager fm, List<T> data) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = data;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
