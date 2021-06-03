package presenters;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import interfaces.SplashScreenInterface;
import model.SplashScreenActivityModel;

import static registration.LogInActivity.TAG;

public class SplashScreenActivityPresenter implements SplashScreenInterface.Presenter {

    private SplashScreenInterface.View view;
    private SplashScreenInterface.Model model;

    public SplashScreenActivityPresenter (SplashScreenInterface.View view) {
        this.view = view;
        if(model == null) model = new SplashScreenActivityModel();
    }

    @Override
    public void getCurrentUserPost() {
        FirebaseUser currentUser = model.gerAuth().getCurrentUser();
        model.getDatabase()
            .collection(SplashScreenActivityModel.COLLECTION_EMPLOYEES_NAME)
            .document(currentUser.getEmail()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Map<String, Object> currentUserData = documentSnapshot.getData();
                    String post = (String) currentUserData.get(SplashScreenActivityModel.EMPLOYEES_FIELD_POST);
                    view.setCurrentUserPost(post);
                }
                else {
                    Log.d(TAG, "SplashScreenActivityPresenter.getCurrentUserPost" +  task.getException().toString());
                }
        });
    }
}
