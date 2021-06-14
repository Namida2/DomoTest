package interfaces;

import com.example.domo.order.TableInfo;

public interface DocumentOrdersListenerInterface {
    interface Observable {
        void ordersNotifyAllSubscribers(Object data);
        void ordersSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber);
        void ordersUnSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber);
        void ordersShowNotification(String title, String name);
        TableInfo getTableInfo ();
    }
    interface Subscriber {
        void ordersNotifyMe(Object data);
    }
}
