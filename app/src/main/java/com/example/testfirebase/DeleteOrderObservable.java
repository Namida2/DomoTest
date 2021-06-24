package com.example.testfirebase;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.services.DocumentOrdersListenerService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import cook.model.OrdersFragmentModel;
import interfaces.DeleteOrderInterface;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;

import static registration.LogInActivity.TAG;

public class DeleteOrderObservable implements DeleteOrderInterface.Observable{

    private AtomicBoolean firstCall;
    private String tableName;
    private OrderActivityModel model = new OrderActivityModel();
    private ArrayList<DeleteOrderInterface.Subscriber> subscribers = new ArrayList<>();
    private static final DeleteOrderObservable observable = new DeleteOrderObservable();
    // OrderActivityPresenter must be the first subscriber

    public void startDocumentListening() {
        firstCall = new AtomicBoolean(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(OrderActivityModel.DOCUMENT_DELETE_ORDER_LISTENER)
            .addSnapshotListener( (value, error) -> {
                if (error != null) {
                    Log.d(TAG, "DeleteOrderObservable.DeleteOrderObservable: " + error.getMessage());
                    return;
                }
                if (value != null && value.exists() && value.get(SplashScreenActivityModel.FIELD_TABLE_NAME) != null) {
                    if(!firstCall.get()) {
                        this.tableName = (String) value.get(SplashScreenActivityModel.FIELD_TABLE_NAME);
                        Map<String, ArrayList<OrderItem>> aaaa = model.getNotEmptyTablesOrdersHashMap();
                        Map<String, ArrayList<OrderItem>> bbbbb = model.getAllTablesOrdersHashMap();
                        try {
                            model.getAllTablesOrdersHashMap().get(tableName).clear();
                            model.getNotEmptyTablesOrdersHashMap().remove(tableName);
                        } catch (Exception e) {
                            Log.d(TAG, "OrderActivityPresenter.deleteOrder: " + e.getMessage());
                        }
                        notifySubscribers(tableName);
                    }
                    firstCall.set(false);
                }
            });
    }
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
