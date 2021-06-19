package com.example.testfirebase.mainActivityFragments;

import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.testfirebase.R;
import com.example.testfirebase.services.DocumentDishesListenerService;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import interfaces.ProfileFragmentInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import presenters.ProfileFragmentPresenter;
import tools.Animations;

public class ProfileFragment extends Fragment implements ProfileFragmentInterface.View {

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
        if (!presenter.getAcceptIconState())
            Animations.Companion.showView(acceptImageView);
        RxView.clicks(contentView.findViewById(R.id.need_notify_constraint_layout))
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                if (presenter.getAcceptIconState()){
                    presenter.setAcceptIconState(false);
                    Animations.Companion.showView(acceptImageView);
                    DocumentDishesListenerService.getService().stopForeground(Service.STOP_FOREGROUND_DETACH);
                } else {
                    presenter.setAcceptIconState(true);
                    Animations.Companion.hideView(acceptImageView);
                    DocumentDishesListenerService.getService().myStartForeground();
                }
            });
        RxView.clicks(contentView.findViewById(R.id.out_constraint_layout))
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {

            });
        Animations.Companion.showView(constraintLayout);
        return contentView;
    }
}
