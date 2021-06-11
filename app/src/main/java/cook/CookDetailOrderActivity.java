package cook;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;

import cook.interfaces.CookDetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import cook.presenters.CookDetailOrderItemsActivityPresenter;
import dialogsTools.ErrorAlertDialog;
import tools.Network;

public class CookDetailOrderActivity extends AppCompatActivity implements CookDetailOrderActivityInterface.View {

    private CookDetailOrderActivityInterface.Presenter presenter;
    private String tableNumber;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialization();
    }
    private void initialization () {
        Intent intent = getIntent();
        tableNumber = intent.getStringExtra(DetailOrderActivityModel.EXTRA_TAG);
        presenter = new CookDetailOrderItemsActivityPresenter(this);
        setViewData();
    }
    @Override
    public void showSetDishReadyDialog(ReadyDish dishData) {
        if (Network.isNetworkConnected(this)) {
            new SetDishReadyAlertDialog( dishName -> {
                presenter.setDishState(dishData);
            }, dishData.getOrderItem().getName()).show(getSupportFragmentManager(), "");
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
}
