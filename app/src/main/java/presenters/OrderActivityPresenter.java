package presenters;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;

import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import model.OrderActivityModel;
import tools.Pair;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter {

    private OrderActivityInterface.View view;
    private OrderActivityInterface.Model model;

    public OrderActivityPresenter (OrderActivityInterface.View view) {
        this.view = view;
        if (model == null) {
            model = new OrderActivityModel();
            setViewModelState();
        }
        else view.setOrderRecyclerViewConsumer(model.getNotifyOrderAdapterConsumer());
    }
    @Override
    public void setViewModelState() {
        RecyclerView recyclerView = view.prepareOrderRecyclerView();
        model.setOrderRecyclerView(recyclerView);
        model.setAdapter( (OrderRecyclerViewAdapter) recyclerView.getAdapter() );
        view.setOrderRecyclerViewConsumer(model.getNotifyOrderAdapterConsumer());
    }
    @Override
    public Consumer<Pair<Dish, Pair<String, Integer>>> getNotifyOrderAdapterConsumer() {
        return model.getNotifyOrderAdapterConsumer();
    }
}
