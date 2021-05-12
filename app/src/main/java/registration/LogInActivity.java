package registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.testfirebase.MainActivity;
import com.example.testfirebase.R;

import dialogs.ErrorAlertDialog;
import interfaces.LogIn;
import io.reactivex.rxjava3.disposables.Disposable;
import presenters.LogInPresenter;

import com.jakewharton.rxbinding4.view.RxView;

public class LogInActivity extends AppCompatActivity implements LogIn.View {

    private LogIn.Presenter presenter;
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
        presenter = new LogInPresenter(this);
        RxView.clicks(findViewById(R.id.newAccountButton))
                .subscribe( click -> this.startActivity( new Intent(this, RegistrationActivity.class)) );
        RxView.clicks(findViewById(R.id.logIn))
                .subscribe(click -> presenter.logIn(email.getText().toString(), password.getText().toString()),
                    error -> Log.d(TAG, error.getMessage()));
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
