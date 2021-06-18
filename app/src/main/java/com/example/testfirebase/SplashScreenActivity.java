package com.example.testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cook.CookMainActivity;
import interfaces.SplashScreenInterface;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;
import presenters.SplashScreenActivityPresenter;
import registration.LogInActivity;
import tools.UserData;

import static registration.LogInActivity.TAG;

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenInterface.View {

    private SplashScreenInterface.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String COLLECTION_LISTENERS = "listeners";
        String COLLECTION_ORDERS = "orders";
        String DOCUMENT_DISHES_LISTENER = "dishes_listener";
        String tableName = "table_7";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReferenceTableName = db.collection(COLLECTION_ORDERS).document(tableName);
        DocumentReference documentReferenceListenersTable = db.collection(COLLECTION_LISTENERS).document(DOCUMENT_DISHES_LISTENER);

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

    @Override
    public void setCurrentUserPost(String post) {
        UserData.post = post;
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
