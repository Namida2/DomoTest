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

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


import interfaces.DocumentDishesListenerServiceInterface;
import interfaces.DocumentOrdersListenerInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import model.SplashScreenActivityModel;

import static registration.LogInActivity.TAG;

public class DocumentOrdersListenerService extends Service implements DocumentOrdersListenerInterface.Observable {

    private static int id = 1;
    private static NotificationChannel channel;
    private static String channelId = "Domo_orders";
    private static String channelName = "DomoChannel";
    private static String TABLE = "Столик ";
    private static String READY_TO_SERVE = "готово к подаче";
    public static String NEW_ORDER = "Новый заказ";

    private NotificationManager notificationManager;
    private static ArrayList<DocumentOrdersListenerInterface.Subscriber> subscribers;
    private Map<String, Object> latestData;

    private static DocumentOrdersListenerService service;
    private static Disposable disposable;
    private AtomicBoolean firstCall = new AtomicBoolean(true);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        service = this;
        if (subscribers == null) subscribers = new ArrayList<>();
        if (latestData == null) latestData = new HashMap<>();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        createChannel(notificationManager);
        disposable = getObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(data -> {
                String tableName = (String) data.get(SplashScreenActivityModel.FIELD_TABLE_NAME);
                ordersServiceNotifyAllSubscribers(data.get(SplashScreenActivityModel.FIELD_TABLE_NAME));
            });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "DocumentOrdersListenerService service created");
        return START_STICKY;
    }
    @Override
    public void ordersServiceNotifyAllSubscribers(Object data) {
        for(DocumentOrdersListenerInterface.Subscriber subscriber : subscribers)
            subscriber.ordersServiceNotifyMe(data);
    }
    @Override
    public void ordersServiceSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber) {
        subscribers.add(subscriber);
    }
    @Override
    public void ordersServiceUnSubscribe(DocumentOrdersListenerInterface.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public void ordersServiceShowNotification(String title, String name) {
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
    private void createChannel (NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }
    }
    private Observable<Map<String, Object>> getObservable (){
        return Observable.create(emitter -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
                .document(SplashScreenActivityModel.DOCUMENT_ORDERS_LISTENER_NAME)
                .addSnapshotListener( (snapshot, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Error!");
                        return;
                    }
                    if (snapshot != null && snapshot.exists() && !firstCall.get()) {
                        emitter.onNext(snapshot.getData());
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                    if (firstCall.get() && snapshot != null)
                        latestData = snapshot.getData();
                    firstCall.set(false);
                });
        });
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
        Log.d(TAG, "DocumentOrdersListenerService service destroyed");
    }
}
