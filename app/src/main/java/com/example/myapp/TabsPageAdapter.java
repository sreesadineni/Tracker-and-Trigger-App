package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsPageAdapter extends FragmentPagerAdapter {
    int tabCount;
    public TabsPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new MeetingsFragment();
            case 1: return new ListsFragment();
            case 2: return new NotesFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
