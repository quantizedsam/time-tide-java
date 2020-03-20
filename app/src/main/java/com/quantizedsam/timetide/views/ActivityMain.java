package com.quantizedsam.timetide.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.quantizedsam.timetide.R;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // variables
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    // constants
    private String className = getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle(R.string.activity_main_nav_item_dashboard_title);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.activity_main_nav_drawer_open, R.string.activity_main_nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem miDashboard = navigationView.getMenu().findItem(R.id.activity_main_nav_item_dashboard);
        miDashboard.setChecked(true);
        onNavigationItemSelected(miDashboard);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String strTitle = "";
        if (id == R.id.activity_main_nav_item_dashboard) {
            FragmentDashboard fragmentDashboard = new FragmentDashboard();
            fragmentTransaction.replace(R.id.activity_main_frame_layout, fragmentDashboard);
            strTitle = getString(R.string.activity_main_nav_item_dashboard_title);
        }
        else if (id == R.id.activity_main_nav_item_habits) {
            FragmentHabits fragmentHabits = new FragmentHabits();
            fragmentTransaction.replace(R.id.activity_main_frame_layout, fragmentHabits);
            strTitle = getString(R.string.activity_main_nav_item_analysis_title);
        }

        fragmentTransaction.commit();
        toolbar.setTitle(strTitle);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
