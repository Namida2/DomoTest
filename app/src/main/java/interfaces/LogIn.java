package interfaces;

public interface LogIn {

    interface Model {}

    interface View {
        void onError(int errorCode);
        void onSuccess();
    }

    interface Presenter {
        void login (String email, String password);
        boolean isValid (String email, String password);
    }
}
