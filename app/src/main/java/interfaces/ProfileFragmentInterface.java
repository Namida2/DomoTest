package interfaces;

public interface ProfileFragmentInterface {
    interface Model {
    }
    interface View {

    }
    interface Presenter {
        void setAcceptIconState(boolean state);
        boolean getAcceptIconState();
    }
}
