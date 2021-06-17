package com.example.testfirebase.services.interfaces;

import java.util.Map;

public interface DocumentDishesListenerInterface {
    interface Observable {
        void dishesNotifyAllSubscribers(Object data);
        void dishesSubscribe(DocumentDishesListenerInterface.Subscriber subscriber);
        void dishesUnSubscribe(DocumentDishesListenerInterface.Subscriber subscriber);
        void dishesShowNotification(String title, String name);
    }
    interface Subscriber {
        void dishesNotifyMe(Object data);
        void dishesSetLatestData(Map<String, Object> latestData);
    }
}
