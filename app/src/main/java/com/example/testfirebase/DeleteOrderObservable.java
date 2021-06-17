package com.example.testfirebase;

import java.util.ArrayList;

import interfaces.DeleteOrderInterface;

public class DeleteOrderObservable implements DeleteOrderInterface.Observable{

    private ArrayList<DeleteOrderInterface.Subscriber> subscribers = new ArrayList<>();
    private static final DeleteOrderObservable observable = new DeleteOrderObservable();
    // OrderActivityPresenter must be the first subscriber
    @Override
    public void notifySubscribers(String tableName) {
        for(DeleteOrderInterface.Subscriber subscriber : subscribers) {
            subscriber.deleteOrder(tableName);
        }
    }
    @Override
    public void subscribe(DeleteOrderInterface.Subscriber subscriber) {
        for(int i = 0; i < subscribers.size(); ++i) {
            DeleteOrderInterface.Subscriber mySubscriber = subscribers.get(i);
            if (mySubscriber.getClass() == subscriber.getClass()) {
                subscribers.remove(i);
                break;
            }
        }
        subscribers.add(subscriber);
    }
    @Override
    public void unSubscribe(DeleteOrderInterface.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    public static DeleteOrderInterface.Observable getObservable() {
        return observable;
    }
}
