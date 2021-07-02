package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {

Toolbar toolbar;
TabLayout tabLayout;
TabItem curmusic;
TabItem allmusic;
TabItem playlist;
ViewPager pager;
PagerController pagerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar=findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setTitle("Tab Music");
        tabLayout= findViewById(R.id.tabLayout);
        curmusic= findViewById(R.id.currentMusic);
        allmusic= findViewById(R.id.allMusic);
        playlist= findViewById(R.id.playlist);
        pager= findViewById(R.id.viewpager);
        
        pagerController = new PagerController(getSupportFragmentManager(),tabLayout.getTabCount());
   pager.setAdapter(pagerController);
   tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
       @Override
       public void onTabSelected(TabLayout.Tab tab) {
           pager.setCurrentItem(tab.getPosition());
          // Toast.makeText(getApplicationContext(),"tost",Toast.LENGTH_SHORT).show();
       }

       @Override
       public void onTabUnselected(TabLayout.Tab tab) {

       }

       @Override
       public void onTabReselected(TabLayout.Tab tab) {

       }
   });
   pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}