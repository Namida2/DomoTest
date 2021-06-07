package cook;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;

import java.util.function.Consumer;

import cook.interfaces.DetailOrderActivityInterface;
import cook.model.DetailOrderActivityModel;
import cook.presenters.DetailOrderActivityPresenter;
import tools.Pair;

public class DetailOrderActivity extends AppCompatActivity implements DetailOrderActivityInterface.View {

    private DetailOrderActivityInterface.Presenter presenter;
    private String tableNumber;
    private Pair<String, TableInfo> dishData;

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
        presenter = new DetailOrderActivityPresenter(this);
        setViewData();
    }


    @Override
    public void showSetDishReadyDialog(Pair<OrderItem, TableInfo> dishData) {
        //this.dishData = dishData;
        new SetDishReadyAlertDialog( dishName -> {
            presenter.setDishState(dishData);
        }, dishData.first.getName()).show(getSupportFragmentManager(), "");

    }

    @Override
    public void setViewData() {
        RecyclerView recyclerView = findViewById(R.id.order_items_recycler_view);
        recyclerView.setAdapter(presenter.getAdapter(tableNumber));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }
}
