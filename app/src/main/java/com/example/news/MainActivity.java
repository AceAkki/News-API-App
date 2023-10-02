package com.example.news;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Create a NavController for navigating between fragments
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Set up the AppBarConfiguration to control the action bar
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_tech)
                .build();

        // Set up the BottomNavigationView with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
