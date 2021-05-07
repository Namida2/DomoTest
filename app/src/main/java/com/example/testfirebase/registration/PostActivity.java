package com.example.testfirebase.registration;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import static com.example.testfirebase.registration.Employee.employee;
public class PostActivity extends Activity {

    private final String COOK = "Повар";
    private final String WAITER = "Официант";
    private final String ADMINISTRATOR = "Администратор";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        RxView.clicks(findViewById(R.id.cook))
                .subscribe(item -> registration(COOK));
        RxView.clicks(findViewById(R.id.waiter))
                .subscribe(item -> registration(WAITER));
        RxView.clicks(findViewById(R.id.administrator))
                .subscribe(item -> registration(ADMINISTRATOR));
    }

   private void registration (String post) {
        employee.setPost(post);
        db.runTransaction(transaction -> {
            return transaction.get();
        });
   }
}
