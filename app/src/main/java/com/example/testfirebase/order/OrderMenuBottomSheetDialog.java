package com.example.testfirebase.order;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.testfirebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class OrderMenuBottomSheetDialog extends BottomSheetDialogFragment {

    private static Consumer<Boolean> orderWasAccepted;
    private static Consumer<Boolean> showChangeGuestsCountDialog;

    public static OrderMenuBottomSheetDialog getNewInstance (Consumer<Boolean> orderWasAccepted, Consumer<Boolean> showChangeGuestsCountDialog) {
        OrderMenuBottomSheetDialog dialog = new OrderMenuBottomSheetDialog();
        OrderMenuBottomSheetDialog.orderWasAccepted = orderWasAccepted;
        OrderMenuBottomSheetDialog.showChangeGuestsCountDialog = showChangeGuestsCountDialog;
        return dialog;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_order_menu, null);

        RxView.clicks(contentView.findViewById(R.id.first_action))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                orderWasAccepted.accept(true);
                dismiss();
            });

        RxView.clicks(contentView.findViewById(R.id.second_action))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                showChangeGuestsCountDialog.accept(true);
                dismiss();
            });

        dialog.setContentView(contentView);
        return dialog;
    }
}
