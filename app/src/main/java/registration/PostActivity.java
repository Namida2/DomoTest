package registration;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.rxbinding4.view.RxView;

import static registration.LogInActivity.TAG;

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
        DocumentReference documentEmployee = collectionReferenceEmployee.document(employee.getEmail());
        firebaseAuth.createUserWithEmailAndPassword(employee.getEmail(), employee.getPassword()).addOnCompleteListener(task -> {
            if( task.isSuccessful() ){
                Log.d(TAG, "The employee was registered.");
                documentEmployee.set(employee).addOnCompleteListener(taskDocument -> {
                    if(taskDocument.isSuccessful()){
                        Log.d(TAG, "The employee was created.");
                    }
                    else {
                        Log.d(TAG, task.getException().toString());
                    }
                });
            }
            else {
                 Log.d(TAG, task.getException().toString());
            }
        });
    }
}

