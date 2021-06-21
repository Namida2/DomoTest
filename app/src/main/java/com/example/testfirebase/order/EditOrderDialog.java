package com.example.testfirebase.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.testfirebase.R;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class EditOrderDialog extends DialogFragment {
    private static Consumer<OrderItem> notifyOrderDataChangedConsumer;
    private static Consumer<OrderItem> removeOrderItemConsumer;
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private static OrderItem orderItem;

    public static EditOrderDialog getNewInstance (OrderItem orderItem) {
        isExist.set(true);
        EditOrderDialog.orderItem = orderItem;
        return new EditOrderDialog();
    }

    public void setNotifyOrderDataChangedConsumer (Consumer<OrderItem> notifyOrderDataChangedConsumer ) {
        EditOrderDialog.notifyOrderDataChangedConsumer = notifyOrderDataChangedConsumer;
    }
    public void setRemoveOrderItemConsumer (Consumer<OrderItem> removeOrderItemConsumer ) {
        EditOrderDialog.removeOrderItemConsumer = removeOrderItemConsumer;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.alertDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_edit_order, null);
        TextView dishCount = contentView.findViewById(R.id.dish_count);
        EditText commentary = contentView.findViewById(R.id.commentary);
        TextView dishName = contentView.findViewById(R.id.dish_name);
        Button saveOderItem = contentView.findViewById(R.id.save_order_button);
        Button removeOrderItem = contentView.findViewById(R.id.remove_order_button);
        dishCount.setText(Integer.toString(orderItem.getCount()));
        commentary.setText(orderItem.getCommentary());
        dishName.setText(orderItem.getName().length() > 36 ?
            orderItem.getName().substring(0, 36) + "..."
            :orderItem.getName());

        RxView.clicks(saveOderItem)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                orderItem.setCount(rdishCount.getText());
                notifyOrderDataChangedConsumer.accept(orderItem);
                isExist.set(false);
                this.dismiss();
            });

        RxView.clicks(removeOrderItem)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                removeOrderItemConsumer.accept(orderItem);
                isExist.set(false);
                this.dismiss();
            });

        builder.setView(contentView);
        return builder.create();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isExist.set(false);
    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window == null) return;
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
    }

    public static boolean isExist() {
        return isExist.get();
    }

}
