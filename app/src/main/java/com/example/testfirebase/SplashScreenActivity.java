package com.example.testfirebase;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.services.DocumentOrdersListenerService;

import administrator.EmployeePermissionObservable;
import cook.CookMainActivity;
import dialogsTools.ErrorAlertDialog;
import interfaces.SplashScreenInterface;
import model.SplashScreenActivityModel;
import presenters.SplashScreenActivityPresenter;
import registration.LogInActivity;
import tools.EmployeeData;

import static registration.LogInActivity.TAG;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenInterface.View {

    private SplashScreenInterface.Presenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new EmployeePermissionObservable();

        if(!DocumentOrdersListenerService.isExist())
            try {
                startService(new Intent(this, DocumentOrdersListenerService.class));
            } catch (Exception e) {Log.d(TAG, "SplashScreenActivity: " + e.getMessage());}
        else DocumentOrdersListenerService.getService().myStartForeground();
        if(!DocumentDishesListenerService.isExist()) {
            DocumentDishesListenerService.setServiceCreatedConsumer(isCreated -> {
                presenter = new SplashScreenActivityPresenter(this);
            });
            try {
                startService(new Intent(this, DocumentDishesListenerService.class));
            } catch (Exception e) {
                Log.d(TAG, "SplashScreenActivity: " + e.getMessage());
            }
        } else {
            DocumentDishesListenerService.getService().myStartForeground();
            presenter = new SplashScreenActivityPresenter(this);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setCurrentUserPost(String post) {
        EmployeeData.post = post;
        DocumentDishesListenerService.setPost(post);
        switch (post) {
            case SplashScreenActivityModel.COOK_POST_NAME:
                if( !getPermission()) {
                    unSubscribeFromServices();
                    return;
                }
                startNewActivity(CookMainActivity.class);
                //DocumentDishesListenerService.unSubscribeFromDatabase();
                //stopService(new Intent(this, DocumentDishesListenerService.class));
                break;
            case SplashScreenActivityModel.WAITER_POST_NAME:
                if( !getPermission()) {
                    unSubscribeFromServices();
                    return;
                }
                startNewActivity(MainActivity.class);
                break;
            case SplashScreenActivityModel.ADMINISTRATOR_POST_NAME:
                unSubscribeFromServices();
                startNewActivity(administrator.MainActivity.class);
                break;
            default: createNewUser(); break;
        }
        DeleteOrderObservable.getObservable().startDocumentListening();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void unSubscribeFromServices() {
        try {
            DocumentDishesListenerService.getService().stopForeground(Service.STOP_FOREGROUND_DETACH);
            DocumentDishesListenerService.unSubscribeFromDatabase();
            DocumentDishesListenerService.getService().stopSelf();
            DocumentOrdersListenerService.getService().stopForeground(Service.STOP_FOREGROUND_DETACH);
            DocumentOrdersListenerService.unSubscribeFromDatabase();
            DocumentOrdersListenerService.getService().stopSelf();
        } catch (Exception e ) {
            Log.d(TAG, "unSubscribeFromServices: " + e.getMessage());
        }
    }
    @Override
    public void createNewUser() {
        startNewActivity(LogInActivity.class);
    }
    private void startNewActivity(Class newActivity) {
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreenActivity.this, newActivity);
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }, 80);
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
