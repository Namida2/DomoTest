package com.example.testfirebase.order;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import interfaces.OrderActivityInterface;
import presenters.MenuDialogPresenter;

public class MenuBottomSheetDialog extends BottomSheetDialogFragment implements OrderActivityInterface.MenuDialog.View {

    private OrderActivityInterface.MenuDialog.Presenter presenter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MenuDialogPresenter(this);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog ;
        return super.onCreateDialog(savedInstanceState);
    }
}
