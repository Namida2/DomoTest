package interfaces;

import java.util.Map;

public interface DocumentOrdersListenerInterface {
    interface Observable {
        void ordersServiceNotifyAllSubscribers(Object data);
        void ordersServiceSubscribe(DocumentDishesListenerServiceInterface.Subscriber subscriber);
        void ordersServiceUnSubscribe(DocumentDishesListenerServiceInterface.Subscriber subscriber);
        void ordersServiceShowNotification(String title, String name);
    }
    interface Subscriber {
        void ordersServiceNotifyMe(Object data);
        void ordersServiceSetLatestData(Object latestData);
    }
}
