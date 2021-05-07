package com.example.testfirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.testfirebase.registration.LoginActivity;

public class HelloActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hello);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(HelloActivity.this, LoginActivity.class);
            HelloActivity.this.startActivity(mainIntent);
            HelloActivity.this.finish();
        }, 1600);

    }
}
