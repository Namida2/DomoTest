package registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.MainActivity;
import com.example.testfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.rxbinding4.view.RxView;

import dialogs.ErrorAlertDialog;
import interfaces.Post;
import presenters.PostPresenter;

import static registration.LogInActivity.TAG;
import static tools.Network.isNetworkConnected;

public class PostActivity extends AppCompatActivity implements Post.View {

    private final String COOK = "Повар";
    private final String WAITER = "Официант";
    private final String ADMINISTRATOR = "Администратор";

    private Post.Presenter presenter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new PostPresenter(this);
        RxView.clicks(findViewById(R.id.cook))
                .subscribe(item -> checkNetworkConnection(COOK));
        RxView.clicks(findViewById(R.id.waiter))
                .subscribe(item -> checkNetworkConnection(WAITER));
        RxView.clicks(findViewById(R.id.administrator))
                .subscribe(item -> checkNetworkConnection(ADMINISTRATOR));
    }

    @Override
    public void checkNetworkConnection(String post) {
        if( isNetworkConnected(this) )
            presenter.registration(post);
        else onError(ErrorAlertDialog.INTERNET_CONNECTION);
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, MainActivity.class) );
    }

    @Override
    public void onError(int errorCode) {
        if( !ErrorAlertDialog.isIsExist() ) {
            ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(errorCode);
            errorAlertDialog.show(getSupportFragmentManager(), "");
        }
    }

}

