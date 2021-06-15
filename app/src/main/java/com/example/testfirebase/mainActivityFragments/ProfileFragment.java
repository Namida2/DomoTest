package com.example.testfirebase.mainActivityFragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.testfirebase.R;

import org.jetbrains.annotations.NotNull;

import interfaces.CheckFragmentInterface;
import presenters.CheckFragmentPresenter;

public class ProfileFragment extends Fragment implements CheckFragmentInterface.View{

    private CheckFragmentInterface.Presenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CheckFragmentPresenter(this);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        if(presenter.getView())

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
