package com.example.testfirebase;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cook.CookMainActivity;
import interfaces.DocumentOrdersListenerInterface;
import interfaces.SplashScreenInterface;
import model.SplashScreenActivityModel;
import presenters.SplashScreenActivityPresenter;
import registration.LogInActivity;
import tools.UserData;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenInterface.View {

    private SplashScreenInterface.Presenter presenter;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DocumentDishesListenerService.LocalBinder binder = (DocumentDishesListenerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        startService(new Intent(this, DocumentOrdersListenerService.class));
        if(!DocumentDishesListenerService.isExist()) {
            DocumentDishesListenerService.setServiceCreatedConsumer(isCreated -> {
                presenter = new SplashScreenActivityPresenter(this);
            });
            bindService(new Intent(this, DocumentDishesListenerService.class), );
        } else presenter = new SplashScreenActivityPresenter(this);
    }

    @Override
    public void setCurrentUserPost(String post) {
        UserData.post = post;
        switch (post) {
            case SplashScreenActivityModel.COOK_POST_NAME:
                startNewActivity(CookMainActivity.class);
                DocumentDishesListenerService.unSubscribeFromDatabase();
                stopService(new Intent(this, DocumentDishesListenerService.class));
                break;
            case SplashScreenActivityModel.WAITER_POST_NAME:
                startNewActivity(MainActivity.class);
                break;
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
