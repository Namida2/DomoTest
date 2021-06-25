package registration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

public class AdministratorPasswordDialog extends DialogFragment {

    private static String password;
    private static Consumer<Boolean> acceptPasswordCheckResult;
    private static AtomicBoolean isExit = new AtomicBoolean(false);

    public static AdministratorPasswordDialog getNewInstance(Consumer<Boolean> acceptPasswordCheckResult, String password) {
        isExit.set(false);
        AdministratorPasswordDialog.acceptPasswordCheckResult = acceptPasswordCheckResult;
        AdministratorPasswordDialog.password = password;
        AdministratorPasswordDialog dialog = new AdministratorPasswordDialog();
        dialog.setCancelable(false);
        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.alertDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_administrator_password, null);
        EditText passwordEditText = contentView.findViewById(R.id.password);
        RxView.clicks(contentView.findViewById(R.id.ok))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                if(passwordEditText.getText().toString().equals(password))
                    acceptPasswordCheckResult.accept(true);
                else acceptPasswordCheckResult.accept(false);
            });
        builder.setView(contentView);
        return builder.create();
    }
    public static boolean isExit() {
        return isExit.get();
    }
}
