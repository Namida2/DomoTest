package interfaces;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public interface SplashScreenInterface {
    interface View {
        void setCurrentUserPost(String post);
    }
    interface Model {
        FirebaseFirestore getDatabase();
        FirebaseAuth gerAuth();
    }
    interface Presenter {
        void getCurrentUserPost();
    }
}
