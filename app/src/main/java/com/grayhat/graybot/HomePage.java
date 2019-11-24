package com.grayhat.graybot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class HomePage extends FragmentActivity{

    TextView titleBar;
    ScrollView scrollView;
    BottomNavigationView bottomNavigationView;
    Youtube youtube;
    ViewPager viewPager;
    GoogleNews googleNews;
    AboutUs aboutUs;
    MenuItem prevMenuItem;

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(youtube);
        adapter.addFragment(googleNews);
        adapter.addFragment(aboutUs);
        viewPager.setAdapter(adapter);
    }

    public void switchToFragmentYT() {
        FragmentManager manager = getSupportFragmentManager();
        youtube.setObject(this);
        manager.beginTransaction().replace(R.id.fragmentV, youtube).commit();
    }

    public void switchToFragmentGN() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentV, googleNews).commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        titleBar = (TextView)findViewById(R.id.titleBar);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);
        youtube = new Youtube(this);
        googleNews = new GoogleNews();
        aboutUs = new AboutUs();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    titleBar.setText("YOUTUBE");
                }
                else if(position==1)
                {
                    titleBar.setText("GOOGLE NEWS");
                }
                else if(position==2)
                {
                    titleBar.setText("ABOUT US");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.youtube:
                        titleBar.setText("YOUTUBE");
                        //switchToFragmentYT();
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.googlenews:
                        titleBar.setText("GOOGLE NEWS");
                        //switchToFragmentGN();
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.aboutus:
                        titleBar.setText("ABOUT US");
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }
}
