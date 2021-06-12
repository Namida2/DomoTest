package interfaces;

import java.util.Map;

public interface DocumentOrdersListenerInterface {
    interface Observable {
        void ordersServiceNotifyAllSubscribers(Object data);
        void ordersServiceSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber);
        void ordersServiceUnSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber);
        void ordersServiceShowNotification(String title, String name);
    }
    interface Subscriber {
        void ordersServiceNotifyMe(Object data);
    }
}
