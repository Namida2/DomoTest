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

public class AddDishAlertDialog extends DialogFragment {

    private static Consumer<OrderItem> notifyOrderAdapterConsumer;
    private static Consumer<Boolean> resetIsPressed;
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private static Dish dish;

    public static AddDishAlertDialog getNewInstance (Consumer<OrderItem> consumer, Dish dish,  Consumer<Boolean> resetIsPressed) {
        AddDishAlertDialog.dish = dish;
        AddDishAlertDialog.resetIsPressed = resetIsPressed;
        AddDishAlertDialog.notifyOrderAdapterConsumer = consumer;
        AddDishAlertDialog dialog = new AddDishAlertDialog();
        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.alertDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_add_dish, null);
        TextView dishCount = contentView.findViewById(R.id.dish_count);
        TextView commentary = contentView.findViewById(R.id.commentary);
        TextView dishName = contentView.findViewById(R.id.dish_name);
        Button addToOrderButton = contentView.findViewById(R.id.save_order_button);
        dishName.setText(dish.getName().length() > 36 ?
            dish.getName().substring(0, 36) + "..."
            :dish.getName());

        RxView.clicks(addToOrderButton)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                int count;
                try {
                    count = Integer.parseInt(dishCount.getText().toString());
                    if(count == 0) count = 1;
                } catch (Exception e) {
                    count = 1;
                }
                OrderItem orderItem = new OrderItem(
                    dish, commentary.getText().toString(), count);
                notifyOrderAdapterConsumer.accept(orderItem);
                resetIsPressed.accept(false);
                this.dismiss();
            });

        builder.setView(contentView);
        return builder.create();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        resetIsPressed.accept(false);
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
//    public static boolean isExit () {
//        return isExist.get();
//    }
}
