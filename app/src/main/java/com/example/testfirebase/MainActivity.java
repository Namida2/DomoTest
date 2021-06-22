package com.example.testfirebase;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

import dialogsTools.ErrorAlertDialog;
import interfaces.DeleteOrderInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import tools.EmployeeData;

import static registration.LogInActivity.TAG;

public class MainActivity extends AppCompatActivity implements DeleteOrderInterface.Subscriber {

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavHostController navHostController;
    private Disposable disposable;
    private TextView title;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DeleteOrderObservable.getObservable().subscribe(this);
        getPermission();
        initialisation();

        disposable = getBottomAppBarObservable()
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemId -> {
                NavDestination currentDestination = navHostController.getCurrentDestination();
                switch ( itemId ) {
                    case R.id.tablesFragment:
                        if(currentDestination.getId() != R.id.tablesFragment)
                            navHostController.navigate(R.id.tablesFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                                );
                        title.setText(getResources().getString(R.string.tables));
                        break;
                    case R.id.ordersFragment:
                        if(currentDestination.getId() != R.id.ordersFragment)
                            navHostController.navigate(R.id.ordersFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                            );
                        title.setText(getResources().getString(R.string.orders));
                        break;
                    case R.id.profileFragment:
                        if(currentDestination.getId() != R.id.profileFragment)
                            navHostController.navigate(R.id.profileFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                            );
                        title.setText(getResources().getString(R.string.profile));
                        break;
                }
            }, error -> {
                Log.d(TAG, Thread.currentThread().getName());
            });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showBottomNavigationView();
        //bottomNavigationView.setSelectedItemId();
    }
    private Observable<Integer> getBottomAppBarObservable () {
        return Observable.create(emitter -> {
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                emitter.onNext(item.getItemId());
                return true;
            });
        });
    }
    private void initialisation () {
        title = findViewById(R.id.title);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostController = (NavHostController) navHostFragment.getNavController();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null) disposable.dispose();
    }
    private void showBottomNavigationView () {
        ViewGroup.LayoutParams layoutParams = bottomNavigationView.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof HideBottomViewOnScrollBehavior) {
                HideBottomViewOnScrollBehavior<BottomNavigationView> hideShowBehavior =
                    (HideBottomViewOnScrollBehavior<BottomNavigationView>) behavior;
                hideShowBehavior.slideUp(bottomNavigationView);
            }
        }
    }
    @Override
    public void deleteOrder(String tableName) {
        showBottomNavigationView();
    }

    public boolean getPermission () {
        if(!EmployeeData.permission && !ErrorAlertDialog.isIsExist()) {
            ErrorAlertDialog dialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.PERMISSION_ERROR);
            dialog.setActionConsumer(finish -> {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finishAndRemoveTask();
            });
            dialog.show(getSupportFragmentManager(), "");
            return false;
        }
        return true;
    }
}