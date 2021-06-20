package administrator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class UserMenuBottomSheetDialog extends BottomSheetDialogFragment {

    private static String userName;
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private static final String ACTION_SET_WORKING_STATE = "Разрешить работать с приложением";
    private static final String ACTION_DELETE_EMPLOYEE = "Удалить работника";

    public static UserMenuBottomSheetDialog getNewInstance(String employeeName) {
        isExist.set(true);
        UserMenuBottomSheetDialog.userName = employeeName;
        return new UserMenuBottomSheetDialog();
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_two_actions, null);
        ( (TextView) contentView.findViewById(R.id.title) ).setText(userName);
        ( (Button) contentView.findViewById(R.id.first_action) ).setText(ACTION_SET_WORKING_STATE);
        ( (Button) contentView.findViewById(R.id.second_action) ).setText(ACTION_DELETE_EMPLOYEE);

        RxView.clicks(contentView.findViewById(R.id.first_action))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {

            });

        RxView.clicks(contentView.findViewById(R.id.second_action))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {

            });

        bottomSheetDialog.setContentView(contentView);
        return bottomSheetDialog;
    }

    public static boolean isExit () {
        return isExist.get();
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        isExist.set(false);
    }
}
