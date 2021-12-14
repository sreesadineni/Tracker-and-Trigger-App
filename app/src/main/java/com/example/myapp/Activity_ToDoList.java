package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Activity_ToDoList extends AppCompatActivity {

    TabLayout tabs;
    TabItem meetingsTab, listsTab, notesTab;
    ViewPager viewpager;

    TabsPageAdapter tabsPageAdapter;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__to_do_list);

        viewpager = (ViewPager)findViewById(R.id.view_pager);

        tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.getTabAt(0).setIcon(R.drawable.ic_baseline_meeting_room_24);
        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_list_24);
        tabs.getTabAt(2).setIcon(R.drawable.ic_baseline_note_24);

       // tabs.setupWithViewPager(viewpager);
        meetingsTab = (TabItem)findViewById(R.id.meetings_tab);
        listsTab = (TabItem)findViewById(R.id.lists_tab);
        notesTab = (TabItem)findViewById(R.id.notes_tab);

        tabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewpager.setAdapter(tabsPageAdapter);

        //listen for tab selection
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2)
                    tabsPageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //listen for scroll or page change
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }
}