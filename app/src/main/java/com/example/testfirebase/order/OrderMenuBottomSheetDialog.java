package com.example.testfirebase.order;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class OrderMenuBottomSheetDialog extends BottomSheetDialogFragment{

    public static OrderMenuBottomSheetDialog getNewInstance () {
        OrderMenuBottomSheetDialog dialog = new OrderMenuBottomSheetDialog();
        return dialog;
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_order_menu, null);
        dialog.setContentView(contentView);
        return dialog;
    }
}
