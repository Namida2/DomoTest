package com.example.testfirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import cook.CookMainActivity;
import interfaces.SplashScreenInterface;
import model.SplashScreenActivityModel;
import presenters.SplashScreenActivityPresenter;
import registration.LogInActivity;
import tools.UserData;

public class SplashScreenActivity extends Activity implements SplashScreenInterface.View {

    private SplashScreenInterface.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startService(new Intent(this, DocumentListenerService.class));
        DocumentListenerService.setServiceCreatedConsumer(isCreated -> {
            presenter = new SplashScreenActivityPresenter(this);
        });
//        new Handler().postDelayed(() -> {
//            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
//            SplashScreenActivity.this.startActivity(mainIntent);
//            SplashScreenActivity.this.finish();
//        }, 80);
    }
    @Override
    public void setCurrentUserPost(String post) {
        UserData.post = post;
        switch (post) {
            case SplashScreenActivityModel.COOK_POST_NAME:
                startNewActivity(CookMainActivity.class);
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
