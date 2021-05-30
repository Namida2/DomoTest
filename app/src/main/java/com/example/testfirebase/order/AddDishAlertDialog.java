package com.example.testfirebase.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import java.util.function.Supplier;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import tools.Pair;

import static registration.LogInActivity.TAG;

public class AddDishAlertDialog extends DialogFragment {

    private static Consumer<Pair<Dish, Pair<String, Integer>>> notifyOrderAdapterConsumer;
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private Dish dish;

    public static AddDishAlertDialog getNewInstance (Consumer<Pair<Dish, Pair<String, Integer>>> consumer) {
        //isExist.set(true);
        AddDishAlertDialog.notifyOrderAdapterConsumer = consumer;
        AddDishAlertDialog dialog = new AddDishAlertDialog();
        //dialog.setCancelable(false);
        return dialog;
    }
    public void setDish(Dish dish) {
        this.dish = dish;
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
        Button addToOrderButton = contentView.findViewById(R.id.add_to_order_button);
        dishName.setText(dish.getName().length() > 36 ?
            dish.getName().substring(0, 36) + "..."
            :dish.getName());

        RxView.clicks(addToOrderButton)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                Pair<Dish, Pair<String, Integer>> order = new Pair<>(dish,
                    new Pair<>(commentary.getText().toString(),
                        Integer.parseInt(dishCount.getText().toString())));
                notifyOrderAdapterConsumer.accept(order);
                Log.d(TAG, Thread.currentThread().getName());
            });

        builder.setView(contentView);
        return builder.create();
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
    public static boolean isExit () {
        return isExist.get();
    }
}
