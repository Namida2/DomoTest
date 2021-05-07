package com.example.testfirebase.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.testfirebase.R;
import com.example.testfirebase.tools.ErrorAlertDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.testfirebase.registration.LoginActivity.TAG;
import static com.example.testfirebase.tools.Network.isNetworkConnected;

public class RegistrationActivity extends AppCompatActivity {

    private final String EMPTY_FIELD = "Пожалуйста, заполните все поля";
    private final String CONFIRM_PASSWORD = "Пожалуйста, проверьте правильность пароля";

    private Disposable textFieldsDisposable;
    private Disposable buttonNextDisposable;
    private Employee employee;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button next;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        fragmentManager = getSupportFragmentManager();
        employee = new Employee();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        next = findViewById(R.id.next);
        next.setOnClickListener(click -> {
            startActivity(new Intent(this, PostActivity.class));
        });
        next.setBackground(getDrawable(R.drawable.bg_button_not_enable));
        next.setEnabled(false);
        textFieldsDisposable = (RxTextView.afterTextChangeEvents(name)
            .mergeWith(RxTextView.afterTextChangeEvents(email))
            .mergeWith(RxTextView.afterTextChangeEvents(confirmPassword))
            .mergeWith(RxTextView.afterTextChangeEvents(password))
            .subscribe(item -> {
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                    || password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()){
                    next.setBackground(getDrawable(R.drawable.bg_button_not_enable));
                    next.setEnabled(false);
                }
                else {
                    next.setBackground(getDrawable(R.drawable.bg_button_round));
                    next.setEnabled(true);
                }
            }));

        buttonNextDisposable = RxView.clicks(next)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(next -> {
                if ( !password.getText().toString().equals(confirmPassword.getText().toString())) {
                    if(!ErrorAlertDialog.isIsExist()){
                        ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.PASSWORD_CONFIRM_ERROR);
                        errorAlertDialog.show(fragmentManager, "");
                    }
                }
                else if ( password.getText().toString().length() < 6 ){
                    Log.d(TAG, "Password is too short");
                    if(!ErrorAlertDialog.isIsExist()) {
                        ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.SHORT_PASSWORD);
                        errorAlertDialog.show(fragmentManager, "");
                    }
                }
                else if ( isNetworkConnected(this) ) {
                    registration();
                }
                else {
                    if( !ErrorAlertDialog.isIsExist() ) {
                        ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.INTERNET_CONNECTION);
                        errorAlertDialog.show(fragmentManager, "");
                    }
                }
            }, error -> {
                Log.d(TAG, "ButtonNext: " + error.getMessage());
            }, () ->{});
    }

    private void registration () {
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        if ( !isEmailValid(emailString) ) {
            Log.d(TAG, "Email is invalid");
            if ( !ErrorAlertDialog.isIsExist() ) {
                ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.WRONG_EMAIL);
                errorAlertDialog.show(fragmentManager, "");
            }
            return;
        }
        firebaseAuth.fetchSignInMethodsForEmail(emailString).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    Log.d(TAG, "NEW");
                    firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener(taskRegistration -> {
                            if (taskRegistration.isSuccessful()) {
                                Log.d(TAG, "SUCCESS");
                            } else {
                                Log.d(TAG, taskRegistration.getException().toString());
                            }
                        });
                } else {
                    Log.d(TAG, "EXIST");
                    if( !ErrorAlertDialog.isIsExist() ){
                        ErrorAlertDialog errorAlertDialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.EMAIL_ALREADY_EXIST);
                        errorAlertDialog.show(fragmentManager, "");
                    }
                }
            } else {
                Log.d(TAG, task.getException().toString());
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textFieldsDisposable.dispose();
        buttonNextDisposable.dispose();
    }
}
