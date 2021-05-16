package interfaces;

public interface RegistrationActivityInterface {

    interface Model {}

    interface View {
        void onSuccess();
        void onError(int errorCode);
    }
    interface Presenter {
        void createEmployee (String name, String email, String password, String confirmPassword);
        boolean isValid (String email, String password, String confirmPassword);
    }

}
