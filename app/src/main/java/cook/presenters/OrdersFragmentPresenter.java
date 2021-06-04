package cook.presenters;

import cook.interfaces.OrdersFragmentInterface;
import cook.model.OrdersFragmentModel;
import interfaces.OrderActivityInterface;
import presenters.OrderActivityPresenter;

public class OrdersFragmentPresenter implements OrdersFragmentInterface.Presenter {

    private OrdersFragmentInterface.View view;
    private OrdersFragmentInterface.Model model;
    private OrderActivityInterface.Presenter orderActivityPresenter;

    public OrdersFragmentPresenter (OrdersFragmentInterface.View view) {
        this.view = view;
        if(model == null) {
            model = new OrdersFragmentModel();
            orderActivityPresenter = new OrderActivityPresenter();
            model.setOrdersHashMap(orderActivityPresenter.getOrdersHashMap());
        }
    }

}
