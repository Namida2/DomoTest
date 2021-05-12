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

    private void isNetworkConnected (String post) {
       if()
    }
}

