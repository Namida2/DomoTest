package presenters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import administrator.model.UsersFragmentModel;
import interfaces.PostActivityInterface;
import model.SplashScreenActivityModel;
import registration.Employee;
import tools.Constants;

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
                DocumentReference docRefDataPassword = db.collection(Constants.COLLECTION_DATA)
                    .document(Constants.DOCUMENT_PASSWORD);
                AtomicReference<String> password = new AtomicReference<>("");
                documentEmployeesListener.update(UsersFragmentModel.FIELD_EMPLOYEE, null)
                    .addOnCompleteListener(taskNull -> {
                        if(taskNull.isSuccessful()) {
                            db.runTransaction(transaction -> {
                                DocumentSnapshot documentSnapshot = transaction.get(docRefDataPassword);
                                try {
                                    password.set((String) documentSnapshot.getData().get(Constants.FIELD_ADMINISTRATOR));
                                } catch (Exception e) {
                                    Log.d(TAG, "PostActivityPresenter.registration: password getting error" );
                                }
                                String email = employee.getEmail().toLowerCase();
                                transaction.set(documentEmployees, employee);
                                if(!post.equals(SplashScreenActivityModel.ADMINISTRATOR_POST_NAME))
                                    transaction.update(documentEmployeesListener,
                                        UsersFragmentModel.FIELD_EMPLOYEE, employee.getEmail());

                                return true;
                            }).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful() && !password.get().equals("")) {
                                    if( post.equals(SplashScreenActivityModel.ADMINISTRATOR_POST_NAME)) {
                                        view.showAdministratorPasswordDialog(password.get());
                                        Log.d(TAG, "The employee was created.");
                                    } else view.onSuccess();
                                }
                                else Log.d(TAG, "PostActivityPresenter.registration: " + task1.getException());
                            });
                        }  else Log.d(TAG, "PostActivityPresenter.registration: " + taskNull.getException());
                   });

            }
            else {
                Log.d(TAG, "PostActivityPresenter.registration: " + task.getException());
            }
        });
    }
}
