package registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.domo.MainActivity;
import com.example.domo.R;

import dialogsTools.ErrorAlertDialog;
import interfaces.LogInActivityInterface;
import io.reactivex.rxjava3.disposables.Disposable;
import presenters.LogInActivityPresenter;

import com.jakewharton.rxbinding4.view.RxView;

import static tools.Network.isNetworkConnected;

public class LogInActivity extends AppCompatActivity implements LogInActivityInterface.View {

    private LogInActivityInterface.Presenter presenter;
    public static final String TAG = "myLogs";

    private EditText email;
    private EditText password;

    private Disposable logInButtonDisposable;
    private Disposable newAccountButtonDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        presenter = new LogInActivityPresenter(this);
        RxView.clicks(findViewById(R.id.newAccountButton))
                .subscribe( click -> this.startActivity( new Intent(this, RegistrationActivity.class)) );
        RxView.clicks(findViewById(R.id.logIn))
                .subscribe(click -> checkNetworkConnection(),
                    error -> Log.d(TAG, error.getMessage()));
    }

    private void checkNetworkConnection () {
        if( isNetworkConnected (this) )
            presenter.logIn(email.getText().toString(), password.getText().toString());
        else onError(ErrorAlertDialog.INTERNET_CONNECTION);
    }

    @Override
    public void onError(int errorCode) {
        if( !ErrorAlertDialog.isIsExist() ) {
            ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(errorCode);
            errorAlertDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
