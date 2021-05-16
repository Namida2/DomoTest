package presenters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import dialogs.ErrorAlertDialog;
import interfaces.LogInActivityInterface;

public class LogInPresenter implements LogInActivityInterface.Presenter {

    private LogInActivityInterface.View view;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "myLogs";
    public LogInPresenter (LogInActivityInterface.View view) {
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void logIn(String email, String password) {
        if( !isValid(email, password) ) {
            view.onError(ErrorAlertDialog.EMPTY_FIELD);
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    view.onSuccess();
                else {
                    Log.d(TAG, "Wrong email or password." );
                    Log.d(TAG, task.getException().toString() );
                    view.onError(ErrorAlertDialog.WRONG_EMAIL_OR_PASSWORD);
                }
            });
        }
    }

    @Override
    public boolean isValid(String email, String password) {
        if(email.isEmpty() || password.isEmpty() || email.contains(" ") || password.contains(" ")) {
            return false;
        }
        return true;
    }


}
