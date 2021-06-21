package administrator;

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

import com.example.testfirebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import registration.Employee;

public class UserMenuBottomSheetDialog extends BottomSheetDialogFragment {

    private static Employee employee;
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private static final String ACTION_SET_WORKING_STATE = "Установить разрешение";
    private static final String ACTION_DELETE_EMPLOYEE = "Удалить работника";

    private Consumer<Boolean> firstActionConsumer;
    private Consumer<Employee> secondActionConsumer;

    public static UserMenuBottomSheetDialog getNewInstance(Employee employee) {
        isExist.set(true);
        UserMenuBottomSheetDialog.employee = employee;
        return new UserMenuBottomSheetDialog();
    }
    public void setFirstActionConsumer(Consumer<Boolean> actionConsumer) {
        this.firstActionConsumer = actionConsumer;
    }
    public void setSecondActionConsumer(Consumer<Employee> actionConsumer) {
        this.secondActionConsumer = actionConsumer;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        View contentView = View.inflate(getContext(), R.layout.dialog_two_actions, null);
        ( (TextView) contentView.findViewById(R.id.title) ).setText(employee.getName());
        ( (Button) contentView.findViewById(R.id.first_action) ).setText(ACTION_SET_WORKING_STATE);
        ( (Button) contentView.findViewById(R.id.second_action) ).setText(ACTION_DELETE_EMPLOYEE);

        RxView.clicks(contentView.findViewById(R.id.first_action))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                firstActionConsumer.accept(!employee.getPermission());
                isExist.set(false);
                dismiss();
            });

        RxView.clicks(contentView.findViewById(R.id.second_action))
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                secondActionConsumer.accept(employee);
                isExist.set(false);
                dismiss();
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
