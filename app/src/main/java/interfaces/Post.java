package interfaces;

public interface Post {

    interface Model { }

    interface View {
        void onSuccess ();
        void onError (int errorCode);
        void checkNetworkConnection (String post);


    }
    interface Presenter {
        void registration (String post);
    }


}
