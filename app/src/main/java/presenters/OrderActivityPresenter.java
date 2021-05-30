package presenters;

import interfaces.OrderActivityInterface;
import model.OrderActivityModel;

public class OrderActivityPresenter implements OrderActivityInterface.Presenter {

    private OrderActivityInterface.View view;
    private OrderActivityInterface.Model model;

    public OrderActivityPresenter (OrderActivityInterface.View view) {
        this.view = view;
        if (model == null)
            model = new OrderActivityModel();
    }
    @Override
    public void setViewModelState() {

    }
}
