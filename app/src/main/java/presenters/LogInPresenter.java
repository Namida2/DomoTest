package presenters;

import com.google.firebase.auth.FirebaseAuth;

import dialogs.ErrorAlertDialog;
import interfaces.LogIn;

public class LogInPresenter implements LogIn.Presenter {

    private LogIn.View view;
    private FirebaseAuth firebaseAuth;

    public LogInPresenter (LogIn.View view) {
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password) {
        if( isValid(email, password) ) {
            view.onError(ErrorAlertDialog.EMPTY_FIELD);
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isCanceled())
                    view.onSuccess();
                else
                    view.onError(ErrorAlertDialog.WRONG_EMAIL_OR_PASSWORD);
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
