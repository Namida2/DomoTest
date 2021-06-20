package presenters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import administrator.model.UsersFragmentModel;
import interfaces.PostActivityInterface;
import model.SplashScreenActivityModel;
import registration.Employee;

import static registration.LogInActivity.TAG;

public class PostActivityPresenter implements PostActivityInterface.Presenter{

    private final String COLLECTION_EMPLOYEES = "employees";
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final CollectionReference collectionReferenceEmployee;

    private PostActivityInterface.View view;

    public PostActivityPresenter(PostActivityInterface.View view) {
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReferenceEmployee = db.collection(COLLECTION_EMPLOYEES);
    }

    @Override
    public void registration(String post) {
        Employee employee = Employee.getEmployee();
        employee.setPost(post);
        DocumentReference documentEmployees = collectionReferenceEmployee.document(employee.getEmail());
        DocumentReference documentEmployeesListener = db
            .collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(UsersFragmentModel.DOCUMENT_EMPLOYEES_LISTENER);
        firebaseAuth.createUserWithEmailAndPassword(employee.getEmail(), employee.getPassword()).addOnCompleteListener(task -> {
            if( task.isSuccessful() ){
                Log.d(TAG, "The employee was registered.");

                //documentEmployeesListener.update(UsersFragmentModel.FIELD_EMPLOYEE, null)
                    //.addOnCompleteListener(taskNull -> {
                        //if(taskNull.isSuccessful()) {
                            db.runTransaction(transaction -> {
                                transaction.set(documentEmployees, employee);
                                transaction.update(documentEmployeesListener,
                                    UsersFragmentModel.FIELD_EMPLOYEE, employee.getEmail());
                                return true;
                            }).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()) {
                                    Log.d(TAG, "The employee was created.");
                                    view.onSuccess();
                                }
                                else Log.d(TAG, "PostActivityPresenter.registration: " + task1.getException());
                            });
                        //}  else Log.d(TAG, "PostActivityPresenter.registration: " + taskNull.getException());
                   //});

            }
            else {
                Log.d(TAG, "PostActivityPresenter.registration: " + task.getException());
            }
        });
    }
}
