package cook.fragments;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.testfirebase.R;
import com.example.testfirebase.services.DocumentDishesListenerService;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.google.firebase.auth.FirebaseAuth;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import dialogsTools.ErrorAlertDialog;
import interfaces.ProfileFragmentInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import presenters.ProfileFragmentPresenter;
import registration.LogInActivity;
import tools.Animations;
import tools.EmployeeData;
import tools.Network;

import static registration.LogInActivity.TAG;

public class CookProfileFragment extends Fragment implements ProfileFragmentInterface.View {

    private ProfileFragmentInterface.Presenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfileFragmentPresenter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_profile, container, false);
        ConstraintLayout constraintLayout = contentView.findViewById(R.id.container_constraint_layout);
        ImageView acceptImageView = contentView.findViewById(R.id.accept_image_view);
        ((TextView) contentView.findViewById(R.id.user_name_text_view)).setText(EmployeeData.name);
        ((TextView) contentView.findViewById(R.id.user_email_text_view)).setText(EmployeeData.email);
        ((TextView) contentView.findViewById(R.id.user_post_text_view)).setText(EmployeeData.post);
        if (!presenter.getAcceptIconState())
            Animations.Companion.showView(acceptImageView);
        RxView.clicks(contentView.findViewById(R.id.need_notify_constraint_layout))
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                if (presenter.getAcceptIconState()){
                    presenter.setAcceptIconState(false);
                    Animations.Companion.showView(acceptImageView);
                    DocumentOrdersListenerService.getService().stopForeground(Service.STOP_FOREGROUND_DETACH);
                } else {
                    presenter.setAcceptIconState(true);
                    Animations.Companion.hideView(acceptImageView);
                    DocumentOrdersListenerService.getService().myStartForeground();
                }
            });
        RxView.clicks(contentView.findViewById(R.id.out_constraint_layout))
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(EmployeeData.email, EmployeeData.password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        if(Network.isNetworkConnected(getContext())) {
                            firebaseAuth.signOut();
                            Intent logInActivity = new Intent(getContext(), LogInActivity.class);
                            startActivity(logInActivity);
                        } else if(!ErrorAlertDialog.isIsExist())
                                ErrorAlertDialog.getNewInstance(ErrorAlertDialog.INTERNET_CONNECTION)
                                    .show(getActivity().getSupportFragmentManager(), "");

                    } else {
                        if(!ErrorAlertDialog.isIsExist()) {
                            ErrorAlertDialog.getNewInstance(ErrorAlertDialog.INTERNET_CONNECTION)
                                .show(getActivity().getSupportFragmentManager(), "");
                        }
                        Log.d(TAG, "ProfileFragment.onCreateView" + task.getException().toString());
                    }
                });
            });
        return contentView;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Animations.Companion.showView(view);
    }
}
