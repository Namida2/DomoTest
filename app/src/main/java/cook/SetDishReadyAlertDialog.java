package cook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.testfirebase.R;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SetDishReadyAlertDialog extends DialogFragment {

    private String dishName;

    public SetDishReadyAlertDialog(String dishName) {
        this.dishName = dishName;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.alertDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_set_dish_ready, null);
        builder.setView(contentView);
        Button cancel = contentView.findViewById(R.id.cancel);
        Button accept = contentView.findViewById(R.id.accept);
        ( (TextView)contentView.findViewById(R.id.title) ).setText(dishName);
        RxView.clicks(cancel)
            .debounce(150, TimeUnit.MILLISECONDS)
            .subscribe(unit -> {
                dismiss();
            });
            RxView.clicks(accept)
                .debounce(150, TimeUnit.MILLISECONDS)
                .subscribe(unit -> {

                });
        return builder.create();
    }
}
