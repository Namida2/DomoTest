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

import cook.CookMainActivity;
import interfaces.SplashScreenInterface;
import model.SplashScreenActivityModel;
import presenters.SplashScreenActivityPresenter;
import registration.LogInActivity;
import tools.EmployeeData;

import static registration.LogInActivity.TAG;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenInterface.View {

    private SplashScreenInterface.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(!DocumentOrdersListenerService.isExist())
            try {
                startService(new Intent(this, DocumentOrdersListenerService.class));
            } catch (Exception e) {Log.d(TAG, "SplashScreenActivity: " + e.getMessage());}
        if(!DocumentDishesListenerService.isExist()) {
            DocumentDishesListenerService.setServiceCreatedConsumer(isCreated -> {
                presenter = new SplashScreenActivityPresenter(this);
            });
            try {
                startService(new Intent(this, DocumentDishesListenerService.class));
            } catch (Exception e) {Log.d(TAG, "SplashScreenActivity: " + e.getMessage());}
        } else presenter = new SplashScreenActivityPresenter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setCurrentUserPost(String post) {
        EmployeeData.post = post;
        DocumentDishesListenerService.setPost(post);
        switch (post) {
            case SplashScreenActivityModel.COOK_POST_NAME:
                startNewActivity(CookMainActivity.class);
                //DocumentDishesListenerService.unSubscribeFromDatabase();
                //stopService(new Intent(this, DocumentDishesListenerService.class));
                break;
            case SplashScreenActivityModel.WAITER_POST_NAME:
                startNewActivity(MainActivity.class);
                break;
            case SplashScreenActivityModel.ADMINISTRATOR_POST_NAME:
                DocumentDishesListenerService.getService().stopForeground(Service.STOP_FOREGROUND_DETACH);
                DocumentDishesListenerService.unSubscribeFromDatabase();
                DocumentDishesListenerService.getService().stopSelf();
                DocumentOrdersListenerService.getService().stopForeground(Service.STOP_FOREGROUND_DETACH);
                DocumentOrdersListenerService.unSubscribeFromDatabase();
                DocumentOrdersListenerService.getService().stopSelf();
                startNewActivity(administrator.MainActivity.class);
                break;
            default: createNewUser(); break;
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
}
