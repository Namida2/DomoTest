package registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.testfirebase.R;
import dialogs.ErrorAlertDialog;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import interfaces.Registration;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import presenters.RegistrationPresenter;

import static registration.LogInActivity.TAG;
import static tools.Network.isNetworkConnected;

public class RegistrationActivity extends AppCompatActivity implements Registration.View {

    private Disposable textFieldsDisposable;
    private Disposable buttonNextDisposable;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button next;
    private FragmentManager fragmentManager;

    private Registration.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        presenter = new RegistrationPresenter(this);
        fragmentManager = getSupportFragmentManager();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        next = findViewById(R.id.next);
        next.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        next.setEnabled(false);

        createFieldsObservables();
        createButtonNextObservables();
    }

    private void createFieldsObservables() {
        textFieldsDisposable = RxTextView.afterTextChangeEvents(name).debounce(150, TimeUnit.MILLISECONDS)
            .mergeWith(RxTextView.afterTextChangeEvents(email)).debounce(150, TimeUnit.MILLISECONDS)
            .mergeWith(RxTextView.afterTextChangeEvents(confirmPassword)).debounce(150, TimeUnit.MILLISECONDS)
            .mergeWith(RxTextView.afterTextChangeEvents(password)).debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(item -> {
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                    || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()){
                    next.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    next.setEnabled(false);
                }
                else {
                    next.setBackgroundColor(getResources().getColor(android.R.color.black));
                    next.setEnabled(true);
                }
            }, error -> {
                Log.d(TAG, "createFieldsObservables: " + error.getMessage());
            });
    }

    private void createButtonNextObservables() {
        buttonNextDisposable = RxView.clicks(next)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next -> {
                if( isNetworkConnected(this) )
                    presenter.createEmployee(name.getText().toString(), email.getText().toString(),
                        password.getText().toString(), confirmPassword.getText().toString());
                else onError(ErrorAlertDialog.INTERNET_CONNECTION);
            }, error -> {
                Log.d(TAG, "ButtonNext: " + error.getMessage());
            }, () ->{});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textFieldsDisposable.dispose();
        buttonNextDisposable.dispose();
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, PostActivity.class));
    }

    @Override
    public void onError(int errorCode) {
        if( !ErrorAlertDialog.isIsExist() ){
            ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(errorCode);
            errorAlertDialog.show(fragmentManager, "");
        }
    }
}
