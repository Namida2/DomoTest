package registration;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebase.R;
import com.example.testfirebase.SplashScreenActivity;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.atomic.AtomicBoolean;

import dialogsTools.ErrorAlertDialog;
import interfaces.PostActivityInterface;
import presenters.PostActivityPresenter;

import static tools.Network.isNetworkConnected;

public class PostActivity extends AppCompatActivity implements PostActivityInterface.View {

    private final String COOK = "Повар";
    private final String WAITER = "Официант";
    private final String ADMINISTRATOR = "Администратор";
    private final String PLEASE_WAIT = "Пожалуйста, подождите...";
    private AtomicBoolean alreadySelected = new AtomicBoolean(false);

    private PostActivityInterface.Presenter presenter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new PostActivityPresenter(this);
        RxView.clicks(findViewById(R.id.cook))
                .subscribe(item -> {
                    if (!alreadySelected.get()) {
                        checkNetworkConnection(COOK);
                        alreadySelected.set(true);
                        Toast.makeText(  this, PLEASE_WAIT, Toast.LENGTH_LONG).show();
                    }
                });
        RxView.clicks(findViewById(R.id.waiter))
                .subscribe(item -> {
                    if (!alreadySelected.get()) {
                        checkNetworkConnection(WAITER);
                        alreadySelected.set(true);
                        Toast.makeText(  this, PLEASE_WAIT, Toast.LENGTH_LONG).show();
                    }
                });
        RxView.clicks(findViewById(R.id.administrator))
                .subscribe(item -> {
                    if (!alreadySelected.get()) {
                        checkNetworkConnection(ADMINISTRATOR);
                        alreadySelected.set(true);
                        Toast.makeText(  this, PLEASE_WAIT, Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    public void checkNetworkConnection(String post) {
        if( isNetworkConnected(this) )
            presenter.registration(post);
        else onError(ErrorAlertDialog.INTERNET_CONNECTION);
    }
    @Override
    public void showAdministratorPasswordDialog(String password) {
        if(!AdministratorPasswordDialog.isExit()) {
            AdministratorPasswordDialog.getNewInstance(isSuccess -> {
                if(isSuccess) onSuccess();
                else onError(ErrorAlertDialog.WRONG_ADMINISTRATOR_PASSWORD);
            }, password).show(getSupportFragmentManager(), "");
        }
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

