package com.example.testfirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


import cook.model.OrdersFragmentModel;
import interfaces.DocumentOrdersListenerInterface;
import io.reactivex.rxjava3.disposables.Disposable;
import model.OrderActivityModel;
import model.SplashScreenActivityModel;
import presenters.OrderActivityPresenter;
import tools.Pair;

import static registration.LogInActivity.TAG;

public class DocumentOrdersListenerService extends Service implements DocumentOrdersListenerInterface.Observable {

    private static int id = 1;
    private static final AtomicBoolean isExist = new AtomicBoolean(false);
    private static NotificationChannel channel;
    private static final String channelId = "Domo_orders";
    private static final String channelName = "DomoOrderChannel";
    private static final String TABLE = "Столик ";
    private static final String READY_TO_SERVE = "готово к подаче";
    public static final String NEW_ORDER = "Новый заказ";

    private NotificationManager notificationManager;
    private static ArrayList<DocumentOrdersListenerInterface.Subscriber> subscribers;

    private static DocumentOrdersListenerService service;
    private static Disposable disposable;
    private static ListenerRegistration registration;
    private AtomicBoolean firstCall = new AtomicBoolean(true);
    private static Map<String, Pair<ArrayList<OrderItem>, Boolean>> latestDishData;
    private static TableInfo latestTableInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        isExist.set(true);
        service = this;
        if (subscribers == null) subscribers = new ArrayList<>();
        if (latestDishData == null) latestDishData = new HashMap<>();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        createChannel(notificationManager);
        Notification notification = new NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_email)
            .setColor(getResources().getColor(R.color.fui_transparent))
            .setContentTitle("Служба уведоблений DOMO")
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setAutoCancel(true)
            .addAction(null)
            .build();
        startForeground(777, notification);
        startDocumentListening();
        Log.d(TAG, "DocumentOrdersListenerService: CREATED");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void ordersNotifyAllSubscribers(Object data) {
        for(DocumentOrdersListenerInterface.Subscriber subscriber : subscribers)
            subscriber.ordersNotifyMe(data);
    }
    @Override
    public void ordersSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber) {
        boolean subscriberAlreadyAdded = false;
        for(DocumentOrdersListenerInterface.Subscriber mySubscriber : subscribers)
            if(mySubscriber.getClass() == subscriber.getClass()) {
                subscriberAlreadyAdded = true;
                break;
            }
        if(!subscriberAlreadyAdded) {
            subscribers.add(subscriber);
        }
    }
    private void startDocumentListening() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        registration = db.collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
            .document(SplashScreenActivityModel.DOCUMENT_ORDERS_LISTENER_NAME)
            .addSnapshotListener( (snapshot, error) -> {
                if (error != null) {
                    isExist.set(false);
                    unSubscribeFromDatabase();
                    stopSelf();
                    Log.d(TAG, "startDocumentListening: " + error.getMessage());
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Object data = snapshot.get(SplashScreenActivityModel.FIELD_TABLE_NAME);
                    if(data != null) {
                        String tableName = (String) snapshot.getData().get(SplashScreenActivityModel.FIELD_TABLE_NAME);
                        readTableData(snapshot.getData().get(SplashScreenActivityModel.FIELD_TABLE_NAME), !firstCall.get());
                        if(subscribers != null && subscribers.size() == 0 && !firstCall.get()) {
                            String tableNumber = tableName.substring(tableName
                                .indexOf(OrdersFragmentModel.DELIMITER) + 1);
                            ordersShowNotification(TABLE + tableNumber, DocumentOrdersListenerService.NEW_ORDER);
                        }
                    }
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    try {
                        Log.d(TAG, "Current data: " + disposable.toString());
                        Log.d(TAG, "Current data: " + registration.toString());
                    } catch (Exception e) {}
                } else {
                    Log.d(TAG, "Current data: null");
                }
            });
    }
    @Override
    public void ordersShowNotification(String title, String name) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        Notification notification = new NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_accept)
            .setLargeIcon(icon)
            .setColor(getResources().getColor(R.color.fui_transparent))
            .setContentTitle(title)
            .setContentText(name)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setAutoCancel(true)
            .addAction(null)
            .build();
        notificationManager.notify(id++, notification); //important thing
    }
    private void createChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void readTableData(Object data, boolean needToNotify) {
        String tableName = (String) data;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {
            db.collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
                .document(tableName)
                .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    TableInfo tableInfo = new TableInfo();
                    DocumentSnapshot documentSnapshot = task.getResult();
                    tableInfo.setTableName(documentSnapshot.getId());
                    Map<String, Object> tableIndoHashMap = documentSnapshot.getData();
                    tableInfo.setTableName(tableName);
                    try {
                        tableInfo.setGuestCount((long) tableIndoHashMap.get(OrderActivityPresenter.GUEST_COUNT_KEY));
                        //tableInfo.setIsComplete((boolean) tableIndoHashMap.get(OrderActivityPresenter.IS_COMPLETE_KEY));
                    } catch (Exception e) {
                        Log.d(TAG, "OrderActivityPresenter.setModelDataState tableInfo: " + e.getMessage());
                        e.printStackTrace();
                    }
                    db.collection(OrderActivityModel.COLLECTION_ORDERS_NAME)
                        .document(tableInfo.getTableName())
                        .collection(OrderActivityModel.COLLECTION_ORDER_ITEMS_NAME)
                        .get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            latestTableInfo = tableInfo;
                            latestDishData = new HashMap<>(); // нужно tableInfo подписчикам
                            List<OrderItem> orderItemsList = task1.getResult().toObjects(OrderItem.class);
                            latestDishData.put(tableInfo.getTableName(), new Pair<>(new ArrayList<>(orderItemsList), true));
                            if (needToNotify) ordersNotifyAllSubscribers(latestDishData);
                            else firstCall.set(false);
                        } else
                            Log.d(TAG, "OrderActivityPresenter.setModelDataState: " + task1.getException());
                    });
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "OrderActivityPresenter.setModelDataState: collection orders is empty." );
        }
    }
    public static void unSubscribeFromDatabase() {
        try {
            disposable.dispose();
            registration.remove();
        } catch (Exception e) {
            Log.d(TAG, "unSubscribeFromDatabase: " + e.getMessage() );
        }
    }
    public static Boolean isExist() {
        return isExist.get();
    }
    @Override
    public void ordersUnSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public TableInfo getTableInfo () {
        return latestTableInfo;
    }
    public static DocumentOrdersListenerService getService() {
        return service;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isExist.set(false);
        Log.d(TAG, "DocumentOrdersListenerService service: DESTROYED");
    }
}
