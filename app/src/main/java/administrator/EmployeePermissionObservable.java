package administrator;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import administrator.interfaces.EmployeePermissionInterface;
import model.SplashScreenActivityModel;
import tools.Constants;

import static registration.LogInActivity.TAG;

public class EmployeePermissionObservable implements EmployeePermissionInterface.Observable {

    private ArrayList<EmployeePermissionInterface.Subscriber> subscribers = new ArrayList<>();
    private EmployeePermissionObservable observable = new EmployeePermissionObservable();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void startListeningDocument () {
        db.collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(Constants.DOCUMENT_PERMISSION_LISTENER)
            .addSnapshotListener( (value, error) -> {
                if (error != null) {
                    Log.d(TAG, "EmployeePermissionObservable.DeleteOrderObservable: " + error.getMessage());
                    return;
                }
                if (value != null && value.exists()) {
                    if(value.get(Constants.FIELD_EMPLOYEE) != null) {
                        this.tableName = (String) value.get(SplashScreenActivityModel.FIELD_TABLE_NAME);
                        try {
                            model.getAllTablesOrdersHashMap().get(tableName).clear();
                            model.getNotEmptyTablesOrdersHashMap().remove(tableName);
                        } catch (Exception e) {
                            Log.d(TAG, "OrderActivityPresenter.deleteOrder: " + e.getMessage());
                        }
                        notifySubscribers(tableName);
                    }
                }
            });
    }

    public EmployeePermissionObservable getObservable() {
        return observable;
    }

    @Override
    public void notifySubscribers(String permission) {

    }

    @Override
    public void subscribe(EmployeePermissionInterface.Subscriber subscriber) {

    }

    @Override
    public void unSubscribe(EmployeePermissionInterface.Subscriber subscriber) {

    }
}
