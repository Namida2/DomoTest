package interfaces;

public interface Post {

    interface Model { }

    interface View {
        void onSuccess ();
        void onError (int errorCode);

    }
    interface Presenter {
        void registration (String post);
    }


}
