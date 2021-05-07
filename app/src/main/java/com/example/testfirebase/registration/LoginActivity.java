package com.example.testfirebase.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import com.example.testfirebase.R;

import com.jakewharton.rxbinding4.view.RxView;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //startService( new Intent(this, MyService.class) );
        RxView.clicks(findViewById(R.id.newAccountButton))
            .subscribe(button -> this.startActivity( new Intent(this, RegistrationActivity.class) ));
    }

}
