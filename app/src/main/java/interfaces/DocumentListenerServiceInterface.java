package interfaces;

import java.util.Map;

public interface DocumentListenerServiceInterface {
    interface Observable {
        void notifyAllSubscribers(Map<String, Object> data);
        void subscribe(DocumentListenerServiceInterface.Subscriber subscriber);
        void unSubscribe(DocumentListenerServiceInterface.Subscriber subscriber);
        void showNotification(String title, String name);
    }
    interface Subscriber {
        void notifyMe(Object data);
        void setLatestData (Map<String, Object> latestData);
    }
}
