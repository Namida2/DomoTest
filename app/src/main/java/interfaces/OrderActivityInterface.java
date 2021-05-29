package interfaces;

import com.example.testfirebase.order.Dish;

import java.util.function.Consumer;

import tools.Pair;

public interface OrderActivityInterface {
    interface Model {
        Consumer<Pair<Dish, Pair<String, Integer>>> getNotifyOrderAdapterConsumer ();
    }
    interface View {

    }
    interface Presenter {

    }
}
