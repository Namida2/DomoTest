package com.example.testfirebase;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class TableMenuBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String ACTION_SHOW_DELETE_ORDER_DIALOG = "showDeleteOrderDialog";
    public static final String ACTION_SHOW_DETAIL_ORDER = "showDetailOrder";

    private static Consumer<String> acceptAction;
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private Disposable disposableShowDeleteOrderDialog;
    private Disposable disposableShowDetailOrder;
    private static String tableName;

    public static TableMenuBottomSheetDialog getNewInstance (Consumer<String> acceptAction, String tableName) {
        isExist.set(true);
        TableMenuBottomSheetDialog.acceptAction = acceptAction;
        TableMenuBottomSheetDialog.tableName = tableName;
        return new TableMenuBottomSheetDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_two_actions, null);
        Button showDeleteOrderDialogButton = contentView.findViewById(R.id.first_action);
        Button showDetailButton = contentView.findViewById(R.id.second_action);
        ((TextView) contentView.findViewById(R.id.title)).setText(tableName);
        disposableShowDeleteOrderDialog = RxView.clicks(showDeleteOrderDialogButton)
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(150, TimeUnit.MILLISECONDS)
            .subscribe(unit -> {
                acceptAction.accept(ACTION_SHOW_DELETE_ORDER_DIALOG);
                dismiss();
            });
        disposableShowDetailOrder = RxView.clicks(showDetailButton)
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(150, TimeUnit.MILLISECONDS)
            .subscribe(unit -> {
                acceptAction.accept(ACTION_SHOW_DETAIL_ORDER);
                dismiss();
            });
        dialog.setContentView(contentView);
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isExist.set(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(disposableShowDeleteOrderDialog != null)
            disposableShowDeleteOrderDialog.dispose();
        if(disposableShowDetailOrder != null)
            disposableShowDetailOrder.dispose();
    }
}
