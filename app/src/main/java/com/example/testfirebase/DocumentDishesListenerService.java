package com.example.testfirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


import cook.model.OrdersFragmentModel;
import interfaces.DocumentDishesListenerServiceInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import model.SplashScreenActivityModel;
import tools.HideNotificationService;

import static registration.LogInActivity.TAG;

public class DocumentDishesListenerService extends Service implements DocumentDishesListenerServiceInterface.Observable {

    private static int id = 1;
    private static AtomicBoolean isExist = new AtomicBoolean(false);

    private static NotificationChannel channel;
    private static String channelId = "Domo_dishes";
    private static String channelName = "DomoDishChannel";
    private static String TABLE = "Столик ";
    private static String READY_TO_SERVE = "готово к подаче";

    private NotificationManager notificationManager;
    private static ArrayList<DocumentDishesListenerServiceInterface.Subscriber> subscribers;
    private Map<String, Object> latestData;

    private static DocumentDishesListenerService service;
    private static Consumer<Boolean> serviceCreatedConsumer;
    private static Disposable disposable;
    private static ListenerRegistration registration;
    private final AtomicBoolean firstCall = new AtomicBoolean(true);

    public static void setServiceCreatedConsumer (Consumer<Boolean> serviceCreatedConsumer) {
        DocumentDishesListenerService.serviceCreatedConsumer = serviceCreatedConsumer;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {

        isExist.set(true);
        service = this;
        subscribers = new ArrayList<>();
        latestData = new HashMap<>();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        createChannel(notificationManager);

        Log.i("Test", "Service: onCreate");
        Notification notification = new NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_accept)
            .setColor(getResources().getColor(R.color.fui_transparent))
            .setContentTitle("Служба уведоблений DOMO")
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setAutoCancel(true)
            .addAction(null)
            .build();

        startForeground(777, notification);
        Intent hideIntent = new Intent(this, HideNotificationService.class);
        startService(hideIntent);



        disposable = getObservable()
            .observeOn(Schedulers.computation())
            .subscribeOn(Schedulers.computation())
            .subscribe(data -> {
                dishesNotifyAllSubscribers(data);
                Map<String, Object> newData = new HashMap<>();
                Set<String> keys = data.keySet();
                for (String key : keys) {
                    ArrayList<String> namesArrayList = new ArrayList<>();
                    try {
                        namesArrayList = (ArrayList<String>) data.get(key);
                    } catch (Exception e) {
                        Log.d(TAG, "DocumentListenerService.onCreate: wrong entry format");
                    }
                    Set<String> items = new HashSet<>(namesArrayList);
                    try {
                        items.removeAll((Collection<?>) latestData.get(key));
                    } catch (Exception e) {
                        Log.d(TAG, "DocumentListenerService.onCreate: latestData does not contain key \"" + key + "\"");
                    }
                    newData.put(key, new ArrayList<>(items));
                }
                keys = newData.keySet();
                for (String tableName : keys) {
                    ArrayList<String> dishNames = (ArrayList<String>) newData.get(tableName);
                    for (int i = 0; i < dishNames.size(); ++i) {
                        try {
                            String tableNumber = tableName.substring(tableName
                                .indexOf(OrdersFragmentModel.DELIMITER) + 1);
                            String dishName = dishNames.get(i);
                            dishName = dishName.substring(0, dishName
                                .indexOf(OrdersFragmentModel.DELIMITER));
                            dishesShowNotification(TABLE + tableNumber, dishName + ": " + READY_TO_SERVE);
                        } catch (Exception e) {
                            Log.d(TAG, "DocumentListenerService.onCreate: wrong delimiter location");
                        }
                    }
                }
                latestData = data;
            });

        try {
            serviceCreatedConsumer.accept(true);
        } catch (Exception e) { }
        Log.w(TAG, "DocumentDishesListenerService: Created");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
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
            registration = db.collection(SplashScreenActivityModel.COLLECTION_LISTENERS_NAME)
                .document(SplashScreenActivityModel.DOCUMENT_DISHES_LISTENER_NAME)
                .addSnapshotListener( (snapshot, error) -> {
                if (error != null) {
                    isExist.set(false);
                    unSubscribeFromDatabase();
                    stopSelf();
                    Log.d(TAG, "DocumentDishesListenerService.getObservable: Error!");
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
    public static DocumentDishesListenerService getService() {
        return service;
    }
    @Nullable

    public static void unSubscribeFromDatabase() {
        disposable.dispose();
        registration.remove();
    }
    @Override
    public void dishesNotifyAllSubscribers(Object data) {
        for(DocumentDishesListenerServiceInterface.Subscriber subscriber : subscribers)
            subscriber.notifyMe(data);
    }
    @Override
    public void dishesSubscribe(DocumentDishesListenerServiceInterface.Subscriber subscriber) {
        boolean subscriberAlreadyAdded = false;
        for(DocumentDishesListenerServiceInterface.Subscriber mySubscriber : subscribers)
            if(mySubscriber.getClass() == subscriber.getClass()) {
                subscriberAlreadyAdded = true;
                break;
            }
        if(!subscriberAlreadyAdded) {
            subscribers.add(subscriber);
            subscriber.setLatestData(latestData);
        }
    }
    @Override
    public void dishesUnSubscribe(DocumentDishesListenerServiceInterface.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public void dishesShowNotification(String title, String name) {
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
        //startForeground(id++, new NotificationCompat.Builder(this, channelId).build()); //important thing
        notificationManager.notify(id++, notification); //important thing
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isExist.set(false);
        Log.d(TAG, "DocumentDishesListenerService: destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Boolean isExist() {
        return isExist.get();
    }
}
