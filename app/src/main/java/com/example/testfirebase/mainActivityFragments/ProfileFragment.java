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
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class ProfileFragment extends Fragment {


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_profile, container, false);

        RxView.clicks(contentView.findViewById(R.id.out_constraint_layout))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {

            });

        return contentView;
    }
}
