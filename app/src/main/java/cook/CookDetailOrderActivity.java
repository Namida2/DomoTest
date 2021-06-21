package cook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.SplashScreenActivity;

import cook.interfaces.CookDetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import cook.presenters.CookDetailOrderItemsActivityPresenter;
import dialogsTools.ErrorAlertDialog;
import dialogsTools.AcceptOrCancelDialog;
import tools.EmployeeData;
import tools.Network;

public class CookDetailOrderActivity extends AppCompatActivity implements CookDetailOrderActivityInterface.View {

    private CookDetailOrderActivityInterface.Presenter presenter;
    private String tableNumber;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getPermission();
        initialization();
    }
    private void initialization () {
        Intent intent = getIntent();
        tableNumber = intent.getStringExtra(DetailOrderActivityModel.EXTRA_TAG);
        presenter = new CookDetailOrderItemsActivityPresenter(this);
        setViewData();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showSetDishReadyDialog(ReadyDish dishData) {
        if (Network.isNetworkConnected(this)) {
            new AcceptOrCancelDialog(dishName -> {
                if(getPermission()) presenter.setDishState(dishData);
            }, dishData.getOrderItem().getName(), null).show(getSupportFragmentManager(), "");
        } else onError(ErrorAlertDialog.INTERNET_CONNECTION);

    }
    @Override
    public void setViewData() {
        RecyclerView recyclerView = findViewById(R.id.order_items_recycler_view);
        recyclerView.setAdapter(presenter.getAdapter(tableNumber));
    }
    @Override
    public void onError(int errorCode) {
        if (!ErrorAlertDialog.isIsExist()) {
            ErrorAlertDialog.getNewInstance(errorCode)
                .show(getSupportFragmentManager(), "");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }
    public boolean getPermission () {
        if(!EmployeeData.permission && !ErrorAlertDialog.isIsExist()) {
            ErrorAlertDialog dialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.PERMISSION_ERROR);
            dialog.setActionConsumer(finish -> {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finishAndRemoveTask();
            });
            dialog.show(getSupportFragmentManager(), "");
            return false;
        }
        return true;
    }
}
