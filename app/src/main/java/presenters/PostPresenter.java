package presenters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import interfaces.Post;
import registration.Employee;

import static registration.LogInActivity.TAG;

public class PostPresenter implements Post.Presenter{

    private final String COLLECTION_EMPLOYEES = "employees";
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final CollectionReference collectionReferenceEmployee;

    private Post.View view;

    public PostPresenter(Post.View view) {
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionReferenceEmployee = db.collection(COLLECTION_EMPLOYEES);
    }

    @Override
    public void registration(String post) {
        Employee employee = Employee.getEmployee();
        employee.setPost(post);
        DocumentReference documentEmployee = collectionReferenceEmployee.document(employee.getEmail());
        firebaseAuth.createUserWithEmailAndPassword(employee.getEmail(), employee.getPassword()).addOnCompleteListener(task -> {
            if( task.isSuccessful() ){
                Log.d(TAG, "The employee was registered.");
                documentEmployee.set(employee).addOnCompleteListener(taskDocument -> {
                    if(taskDocument.isSuccessful()) {
                        Log.d(TAG, "The employee was created.");
                    }
                    else {
                        Log.d(TAG, task.getException().toString());
                    }
                });
            }
            else {
                Log.d(TAG, task.getException().toString());
            }
        });
    }
}
