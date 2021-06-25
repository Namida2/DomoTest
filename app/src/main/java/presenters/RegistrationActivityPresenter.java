package presenters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dialogsTools.ErrorAlertDialog;
import interfaces.RegistrationActivityInterface;
import registration.Employee;

import static registration.LogInActivity.TAG;

public class RegistrationActivityPresenter implements RegistrationActivityInterface.Presenter {

    private RegistrationActivityInterface.View view;
    private FirebaseAuth firebaseAuth;

    public RegistrationActivityPresenter(RegistrationActivityInterface.View view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void createEmployee(String name, String email, String password, String confirmPassword) {
        if( isValid(email, password, confirmPassword) ) {
            firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().isEmpty()) {
                        Log.d(TAG, "NEW EMAIL");
                        Employee employee = Employee.getEmployee();
                        employee.setName(name);
                        employee.setEmail(email.toLowerCase());
                        employee.setPassword(password);
                        employee.setPermission(false);
                        view.onSuccess();
                    } else {
                        Log.d(TAG, "EXIST EMAIL");
                        view.onError(ErrorAlertDialog.EMAIL_ALREADY_EXIST);
                    }
                } else {
                    Log.d(TAG, task.getException().toString());
                }
            });
        }
    }

    @Override
    public boolean isValid(String email, String password, String confirmPassword) {
        if( !isEmailValid(email) ) {
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
        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
