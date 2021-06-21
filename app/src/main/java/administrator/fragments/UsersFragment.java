package administrator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;

import org.jetbrains.annotations.NotNull;

import administrator.UserMenuBottomSheetDialog;
import administrator.interfaces.UsersFragmentInterface;
import administrator.presenter.UsersFragmentPresenter;
import dialogsTools.ErrorAlertDialog;
import registration.Employee;

public class UsersFragment extends Fragment implements UsersFragmentInterface.View {

    private UsersFragmentInterface.Presenter presenter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UsersFragmentPresenter(this);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        presenter.setModelState();
        View contentView = presenter.getView();
        if(contentView != null) return contentView;
        contentView = inflater.inflate(R.layout.fragment_administrator_users, container, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.users_recycle_view);
        recyclerView.setAdapter(presenter.getAdapter());
        presenter.setView(contentView);
        return contentView;
    }
    @Override
    public void showEmployeeDialog(Employee employee) {
        if(!UserMenuBottomSheetDialog.isExit()) {
            UserMenuBottomSheetDialog dialog = UserMenuBottomSheetDialog.getNewInstance(employee);
            dialog.setFirstActionConsumer(permission -> {
                presenter.setUserPermission(employee, permission);
            });
            dialog.setSecondActionConsumer(employee1 -> {
                presenter.deleteUser(employee);
            });
            dialog.show(getActivity().getSupportFragmentManager(), "");
        }
    }
    @Override
    public void onComplete() { }
    @Override
    public void onError(int errorCode) {
        if(!ErrorAlertDialog.isIsExist())
            ErrorAlertDialog.getNewInstance(errorCode)
                .show(getActivity().getSupportFragmentManager(), "");
    }
    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }
}
