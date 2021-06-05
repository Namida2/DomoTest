package presenters;

import android.util.Log;

import com.example.testfirebase.order.TableInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

import interfaces.OrderActivityInterface;
import interfaces.SplashScreenInterface;
import interfaces.ToolsInterface;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;

import static registration.LogInActivity.TAG;

public class SplashScreenActivityPresenter implements SplashScreenInterface.Presenter, ToolsInterface.Notifiable {

    private SplashScreenInterface.View view;
    private SplashScreenInterface.Model splashScreenModel;
    private OrderActivityInterface.Presenter orderActivityPresenter;

    public SplashScreenActivityPresenter (SplashScreenInterface.View view) {
        this.view = view;
        if(splashScreenModel == null) splashScreenModel = new SplashScreenActivityModel();
        orderActivityPresenter = new OrderActivityPresenter(this);
        orderActivityPresenter.setModelDataState(true);

    }
    @Override
    public void notifyMe() {
        ArrayList<TableInfo> asd = orderActivityPresenter.getTableInfoArrayList();
        getCurrentUserPost();
    }
    @Override
    public void getCurrentUserPost() {
        FirebaseUser currentUser = splashScreenModel.gerAuth().getCurrentUser();
        if(currentUser == null || currentUser.getEmail() == null) {
            view.createNewUser();
            return;
        }
        splashScreenModel.getDatabase()
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
