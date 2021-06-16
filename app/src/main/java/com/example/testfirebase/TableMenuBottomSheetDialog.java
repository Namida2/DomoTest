package com.example.testfirebase;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class TableMenuBottomSheetDialog extends BottomSheetDialogFragment {

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_table_menu, null);
        Button deleteOrderButton = contentView.findViewById(R.id.delete_order_button);
        Button showDetailButton = contentView.findViewById(R.id.show_detail_button);
        //  RxView


        dialog.setContentView(contentView);
        return super.onCreateDialog(savedInstanceState);
    }
}
