package dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testfirebase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import interfaces.OrderActivityInterface;

public class GuestsCountBottomSheetDialog extends BottomSheetDialogFragment implements OrderActivityInterface.Dialog.MyView {

    private OrderActivityInterface.Dialog.Presenter presenter;
    private View contentView;
    public GuestsCountBottomSheetDialog (View contentView) {
        this.contentView = contentView;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.bottomSheetDialogStyle);
        dialog.setContentView(contentView);
        return dialog;
    }
}