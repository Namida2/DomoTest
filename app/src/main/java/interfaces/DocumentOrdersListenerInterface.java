package interfaces;

import java.util.Map;

public interface DocumentOrdersListenerInterface {
    interface Observable {
        void ordersNotifyAllSubscribers(Object data);
        void ordersSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber);
        void ordersUnSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber);
        void ordersShowNotification(String title, String name);
    }
    interface Subscriber {
        void ordersNotifyMe(Object data);
    }
}
