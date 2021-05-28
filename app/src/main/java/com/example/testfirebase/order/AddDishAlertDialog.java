package com.example.testfirebase.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.testfirebase.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class AddDishAlertDialog extends DialogFragment {

    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private static Dish dish;

    public static AddDishAlertDialog getNewInstance (Dish dish) {
        //isExist.set(true);
        AddDishAlertDialog.dish = dish;
        AddDishAlertDialog dialog = new AddDishAlertDialog();
        //dialog.setCancelable(false);
        return dialog;
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.alertDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_add_dish, null);
        TextView dishName = contentView.findViewById(R.id.dish_name);
        dishName.setText(dish.getName().length() > 26 ?
            dish.getName().substring(0, 26) + "..."
            :dish.getName());
        builder.setView(contentView);
        return builder.create();
    }
    public static boolean isExit () {
        return isExist.get();
    }
}
