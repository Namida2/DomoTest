package administrator.presenter;

import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import administrator.adapters.UsersRecyclerViewAdapter;
import administrator.interfaces.EmployeePermissionInterface;
import administrator.interfaces.UsersFragmentInterface;
import administrator.model.UsersFragmentModel;
import dialogsTools.ErrorAlertDialog;
import model.SplashScreenActivityModel;
import registration.Employee;
import tools.Constants;
import tools.EmployeeData;

import static android.content.Context.DISPLAY_SERVICE;
import static registration.LogInActivity.TAG;

public class UsersFragmentPresenter implements UsersFragmentInterface.Presenter {

    private UsersFragmentInterface.View view;
    private static UsersFragmentInterface.Model model;
    private AtomicBoolean firstCall = new AtomicBoolean(false);

    public UsersFragmentPresenter (UsersFragmentInterface.View view) {
        this.view = view;
        if(model == null) {
            model = new UsersFragmentModel();
            model.setAdapter(new UsersRecyclerViewAdapter());
        }
        startDocumentEmployeesListener();
        onResume();
    }
    @Override
    public void setUserPermission(Employee employee, boolean permission) {
        model.getAdapter().setEmployeesPermission(employee, permission);
        DocumentReference docRefEmployee = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_EMPLOYEES_NAME)
            .document(employee.getEmail());
        DocumentReference docRefPermissionListener = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(Constants.DOCUMENT_PERMISSION_LISTENER);

        docRefPermissionListener.update(Constants.FIELD_EMPLOYEE, null)
            .addOnCompleteListener(taskNull -> {
                if(taskNull.isSuccessful()) {
                    model.getDatabase().runTransaction(transaction -> {
                        transaction.update(docRefEmployee, Constants.FIELD_PERMISSION, permission);
                        transaction.update(docRefPermissionListener, Constants.FIELD_EMPLOYEE, employee.getEmail());
                        transaction.update(docRefPermissionListener, Constants.FIELD_PERMISSION, permission);
                        return true;
                    }).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "UsersFragmentPresenter.setUserPermission: SUCCESS" );
                        } else Log.d(TAG, "UsersFragmentPresenter.setUserPermission: " + task.getException());
                    });
                } else Log.d(TAG, "UsersFragmentPresenter.setUserPermission: " + taskNull.getException());
            });
    }
    @Override
    public void onResume() {
        model.getAdapter().setActionsConsumer(employee -> {
            view.showEmployeeDialog(employee);
        });
    }
    @Override
    public void setModelState() {
        model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_EMPLOYEES_NAME)
            .whereNotEqualTo(SplashScreenActivityModel.EMPLOYEES_FIELD_POST, SplashScreenActivityModel.ADMINISTRATOR_POST_NAME)
            .get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    List<Employee> employees;
                    employees = task.getResult().toObjects(Employee.class);
                    model.setEmployeesArrayList(employees);
                    model.getAdapter().setEmployeesArrayList(model.getEmployeesArrayList());
                } else Log.d(TAG, "UsersFragmentPresenter.setModelState: " + task.getException());
        });
    }
    public void startDocumentEmployeesListener () {
        model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(UsersFragmentModel.DOCUMENT_EMPLOYEES_LISTENER)
            .addSnapshotListener( (snapshot, error) -> {
                if (error != null) {
                    Log.d(TAG, "UsersFragmentPresenter.startDocumentEmployeesListener: " + error.getMessage());
                    return;
                }
                if (snapshot != null && snapshot.exists() && !firstCall.get()) {
                    readNewUser((String) snapshot.get(UsersFragmentModel.FIELD_EMPLOYEE));
                } else {
                    Log.d(TAG, "Current data: null");
                }
                firstCall.set(false);
            });
    }
    private void readNewUser (String userName) {
        try {
            model.getDatabase()
                .collection(SplashScreenActivityModel.COLLECTION_EMPLOYEES_NAME)
                .document(userName)
                .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Employee employee = task.getResult().toObject(Employee.class);
                    if(!model.getEmployeesArrayList().contains(employee) && employee != null) {
                        model.getEmployeesArrayList().add(employee);
                        model.getAdapter().notifyDataSetChanged();
                    }
                } else Log.d(TAG, "UsersFragmentPresenter.readNewUser: " + task.getException());
            });
        } catch (Exception e) {
            Log.d(TAG, "UsersFragmentPresenter.readNewUser: " + e.getMessage());
        }
    }
    @Override
    public void setView(View view) {
        model.setView(view);
    }
    @Override
    public View getView() {
        return model.getView();
    }

    @Override
    public void deleteUser(Employee employee) {
        boolean permission = false;
        model.getAdapter().setEmployeesPermission(employee, permission);
        DocumentReference docRefEmployee = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_EMPLOYEES_NAME)
            .document(employee.getEmail());
        DocumentReference docRefPermissionListener = model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(Constants.DOCUMENT_PERMISSION_LISTENER);
        docRefPermissionListener.update(Constants.FIELD_EMPLOYEE, null)
            .addOnCompleteListener(taskNull -> {
                if(taskNull.isSuccessful()) {
                    model.getDatabase().runTransaction(transaction -> {
                        transaction.update(docRefPermissionListener, Constants.FIELD_EMPLOYEE, employee.getEmail());
                        transaction.update(docRefPermissionListener, Constants.FIELD_PERMISSION, permission);
                        transaction.delete(docRefEmployee);
                        return true;
                    }).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            model.getEmployeesArrayList().remove(employee);
                            model.getAdapter().notifyDataSetChanged();
                            Log.d(TAG, "UsersFragmentPresenter.setUserPermission: SUCCESS");
                        } else Log.d(TAG, "UsersFragmentPresenter.setUserPermission: " + task.getException());
                    });
                } else Log.d(TAG, "UsersFragmentPresenter.setUserPermission: " + taskNull.getException());
            });
    }

    @Override
    public UsersRecyclerViewAdapter getAdapter() {
        return model.getAdapter();
    }

}
