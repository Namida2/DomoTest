package presenters;

import interfaces.ProfileFragmentInterface;
import model.ProfileFragmentModel;

public class ProfileFragmentPresenter implements ProfileFragmentInterface.Presenter {

    private ProfileFragmentInterface.View view;
    private ProfileFragmentInterface.Model model;

    public ProfileFragmentPresenter(ProfileFragmentInterface.View view) {
        this.view = view;
    }
    @Override
    public void setAcceptIconState(boolean state) {
        ProfileFragmentModel.NEED_NOTIFY.set(state);
    }
    @Override
    public boolean getAcceptIconState() {
        return ProfileFragmentModel.NEED_NOTIFY.get();
    }
}
