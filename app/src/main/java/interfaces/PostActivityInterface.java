package interfaces;

public interface PostActivityInterface {

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
