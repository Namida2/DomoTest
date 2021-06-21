package administrator;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import administrator.interfaces.EmployeePermissionInterface;
import dialogsTools.ErrorAlertDialog;
import model.SplashScreenActivityModel;
import registration.Employee;
import tools.Constants;
import tools.EmployeeData;

import static registration.LogInActivity.TAG;

public class EmployeePermissionObservable {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String employeeEmail = "";
    private static boolean permission;

    public EmployeePermissionObservable () {
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
                        if(employeeEmail.equals(EmployeeData.email))
                            EmployeeData.permission = permission;
                    }
                    Log.d(TAG, "EmployeePermissionObservable.startListeningDocument: " + employeeEmail + ", " + permission);
                }
            });
    }

}
