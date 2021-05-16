package com.example.testfirebase;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavHostController navHostController;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialisation();
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.tablesFragment:
                    navHostController.navigate(R.id.tablesFragment,
                            null,
                            new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build());
                    return true;
                case R.id.ordersFragment:
                    navHostController.navigate(R.id.ordersFragment,
                            null,
                            new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build());
                    return true;
                case R.id.checkFragment:
                    navHostController.navigate(R.id.checkFragment,
                            null, new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build());
                    return true;
                case R.id.profileFragment:
                    navHostController.navigate(R.id.profileFragment,
                            null, new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build());
                    return true;
            }
            return false;
        });
    }

    private void initialisation () {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostController = (NavHostController) navHostFragment.getNavController();
    }

}
