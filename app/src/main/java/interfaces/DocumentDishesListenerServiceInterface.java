package interfaces;

import java.util.Map;

public interface DocumentDishesListenerServiceInterface {
    interface Observable {
        void dishesNotifyAllSubscribers(Object data);
        void dishesSubscribe(DocumentDishesListenerServiceInterface.Subscriber subscriber);
        void unSubscribe(DocumentDishesListenerServiceInterface.Subscriber subscriber);
        void showNotification(String title, String name);
    }
    interface Subscriber {
        void notifyMe(Object data);
        void setLatestData (Map<String, Object> latestData);
    }
}
