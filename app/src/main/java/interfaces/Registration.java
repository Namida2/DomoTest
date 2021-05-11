package interfaces;

public interface Registration {

    interface Model {}

    interface View {
        void onSuccess();
        void onError(int errorCode);
    }
    interface Presenter {
        void createEmployee (String email, String password, String confirmPassword);
        boolean isValid (String email, String password, String confirmPassword);
    }

}
