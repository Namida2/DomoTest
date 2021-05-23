package presenters;

import com.example.testfirebase.order.Dish;

import interfaces.OrderActivityInterface;
import model.MenuDialogModel;

public class MenuDialogPresenter implements OrderActivityInterface.MenuDialog.Presenter {

    private OrderActivityInterface.MenuDialog.View view;
    private OrderActivityInterface.MenuDialog.Model model;

    public MenuDialogPresenter (OrderActivityInterface.MenuDialog.View view) {
        this.view = view;
        this.model = new MenuDialogModel();

    }

}
