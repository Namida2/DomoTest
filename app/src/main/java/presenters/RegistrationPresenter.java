package presenters;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dialogs.ErrorAlertDialog;
import interfaces.Registration;
import registration.Employee;
import registration.PostActivity;

import static registration.Employee.employee;
import static registration.LogInActivity.TAG;

public class RegistrationPresenter implements Registration.Presenter {

    private Registration.View view;
    private FirebaseAuth firebaseAuth;

    public RegistrationPresenter (Registration.View view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void createEmployee(String email, String password, String confirmPassword) {
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    Log.d(TAG, "NEW EMAIL");
                    Employee.getEmployee();
                    employee.setName(nameString);
                    employee.setEmail(emailString);
                    employee.setPassword(passwordString);
                    startActivity(new Intent(this, PostActivity.class));
                } else {
                    Log.d(TAG, "EXIST EMAIL");
                }
            } else {
                Log.d(TAG, task.getException().toString());
            }
        });
    }

    @Override
    public boolean isValid(String email, String password, String confirmPassword) {
        if( isEmailValid(email) ) {
            Log.d(TAG, "Email is invalid");
            view.onError(ErrorAlertDialog.WRONG_EMAIL);
            return false;
        }
        if ( password.length() < 6 ) {
            Log.d(TAG, "Password is too short.");
            view.onError(ErrorAlertDialog.SHORT_PASSWORD);
            return false;
        }
        if ( !password.equals(confirmPassword) ) {
            Log.d(TAG, "Invalid password  confirmation.");
            view.onError(ErrorAlertDialog.PASSWORD_CONFIRM_ERROR);
            return false;
        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
