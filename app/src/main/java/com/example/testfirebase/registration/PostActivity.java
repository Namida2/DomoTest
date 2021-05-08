package com.example.testfirebase.registration;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import static com.example.testfirebase.registration.Employee.employee;
import static com.example.testfirebase.registration.LoginActivity.TAG;

public class PostActivity extends Activity {

    private final String COOK = "Повар";
    private final String WAITER = "Официант";
    private final String ADMINISTRATOR = "Администратор";
    private final String COLLECTION_EMPLOYEES = "employees";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReferenceEmployee = db.collection(COLLECTION_EMPLOYEES);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RxView.clicks(findViewById(R.id.cook))
                .subscribe(item -> registration(COOK));
        RxView.clicks(findViewById(R.id.waiter))
                .subscribe(item -> registration(WAITER));
        RxView.clicks(findViewById(R.id.administrator))
                .subscribe(item -> registration(ADMINISTRATOR));
    }

    private void registration (String post) {
        employee.setPost(post);
        DocumentReference document = collectionReferenceEmployee.document(employee.getEmail());
        db.runTransaction(transaction -> {
            firebaseAuth.createUserWithEmailAndPassword(employee.getEmail(), employee.getPassword());
            firebaseAuth.signInWithEmailAndPassword(employee.getEmail(), employee.getPassword()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Log.d(TAG, "SUCCESS");
                }
                else Log.d(TAG, task.getException().toString());
            });;
            if( firebaseAuth.getCurrentUser() != null )
                Log.d(TAG, firebaseAuth.getCurrentUser().toString());
            else Log.d(TAG, "ERROR");

            document.set(employee);
            return true;
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

            }
            else Log.d(TAG, task.getException().toString());
        });
    }
}
