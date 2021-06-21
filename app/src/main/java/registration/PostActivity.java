package registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.R;
import com.example.testfirebase.SplashScreenActivity;
import com.jakewharton.rxbinding4.view.RxView;

import dialogsTools.ErrorAlertDialog;
import interfaces.PostActivityInterface;
import presenters.PostActivityPresenter;

import static tools.Network.isNetworkConnected;

public class PostActivity extends AppCompatActivity implements PostActivityInterface.View {

    private final String COOK = "Повар";
    private final String WAITER = "Официант";
    private final String ADMINISTRATOR = "Администратор";

    private PostActivityInterface.Presenter presenter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new PostActivityPresenter(this);
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
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onError(int errorCode) {
        if( !ErrorAlertDialog.isIsExist() ) {
            ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(errorCode);
            errorAlertDialog.show(getSupportFragmentManager(), "");
        }
    }

}

