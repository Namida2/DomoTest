package administrator;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import administrator.interfaces.EmployeePermissionInterface;
import dialogsTools.ErrorAlertDialog;
import model.SplashScreenActivityModel;
import tools.Constants;
import tools.EmployeeData;

import static registration.LogInActivity.TAG;

public class EmployeePermissionObservable implements EmployeePermissionInterface.Observable {

    private ArrayList<EmployeePermissionInterface.Subscriber> subscribers = new ArrayList<>();
    private EmployeePermissionObservable observable = new EmployeePermissionObservable();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String employeeEmail;
    private boolean permission;

    EmployeePermissionObservable () {
        startListeningDocument();
    }
    private void startListeningDocument () {
        db.collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(Constants.DOCUMENT_PERMISSION_LISTENER)
            .addSnapshotListener( (value, error) -> {
                if (error != null) {
                    Log.d(TAG, "EmployeePermissionObservable.startListeningDocument: " + error.getMessage());
                    return;
                }
                if (value != null && value.exists()) {
                    if(value.get(Constants.FIELD_EMPLOYEE) != null && value.get(Constants.FIELD_PERMISSION) != null) {
                        this.employeeEmail = (String) value.get(Constants.FIELD_EMPLOYEE);
                        this.permission = (boolean) value.get(Constants.FIELD_PERMISSION);
                        if(EmployeeData.email.equals(employeeEmail) || !permission)
                            notifySubscribers();
                    }
                    Log.d(TAG, "EmployeePermissionObservable.startListeningDocument: " + employeeEmail + ", " + permission);
                }
            });
    }
    public EmployeePermissionObservable getObservable() {
        return observable;
    }
    @Override
    public void notifySubscribers() {
        for(EmployeePermissionInterface.Subscriber subscriber : subscribers) {
            subscriber.acceptPermission(employeeEmail, permission);
        }
    }
    @Override
    public void subscribe(EmployeePermissionInterface.Subscriber newSubscriber) {
        for(int i = 0; i < subscribers.size(); ++i) {
            EmployeePermissionInterface.Subscriber subscriber = subscribers.get(i);
            if(newSubscriber.getClass() == subscriber.getClass()) {
                subscribers.remove(i);
                break;
            }
        }
        subscribers.add(newSubscriber);
        if(EmployeeData.email.equals(employeeEmail) || !permission)
            newSubscriber.acceptPermission(employeeEmail, permission);
    }
    @Override
    public void unSubscribe(EmployeePermissionInterface.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
