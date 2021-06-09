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
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cook.model.DetailOrderActivityModel;
import interfaces.DocumentListenerServiceInterface;
import interfaces.ToolsInterface;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static registration.LogInActivity.TAG;

public class DocumentListenerService extends Service implements DocumentListenerServiceInterface.Observable {

    private static int id = 1;
    private static NotificationChannel channel;
    private static String channelId = "Domo";
    private static String channelName = "DomoChannel";

    private NotificationManager notificationManager;
    private static ArrayList<DocumentListenerServiceInterface.Subscriber> subscribers;
    private Map<String, Object> latestData;

    private static DocumentListenerService service;

    @Override
    public void onCreate() {
        service = this;
        subscribers = new ArrayList<>();
        latestData = new HashMap<>();
        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        createChannel(notificationManager);
        getObservable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe(data ->{
                latestData = data;
                notifyAllSubscribers(data);
            });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service created");
        return START_STICKY;
    }
    @Override
    public void notifyAllSubscribers(Map<String, Object> data) {
        for(DocumentListenerServiceInterface.Subscriber subscriber : subscribers) {
            subscriber.notifyMe(data);
        }
    }
    @Override
    public void subscribe(DocumentListenerServiceInterface.Subscriber subscriber) {
        subscribers.add(subscriber);
        subscriber.setLatestData(latestData);
    }
    @Override
    public void unSubscribe(DocumentListenerServiceInterface.Subscriber subscriber) {
        subscribers.remove(subscriber);
    }
    @Override
    public void showNotification(String title, String name) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        Notification notification = new NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_accept)
            .setLargeIcon(icon)
            .setColor(getResources().getColor(R.color.fui_transparent))
            .setContentTitle(title)
            .setContentText(name)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .build();
        notificationManager.notify(id, notification);// important thing
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
            db.collection(DetailOrderActivityModel.COLLECTION_ITEMS_LISTENER_NAME)
                .document(DetailOrderActivityModel.DOCUMENT_LISTENER_NAME)
                .addSnapshotListener( (snapshot, error) -> {
                if (error != null) {
                    Log.d(TAG, "Error!");
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    emitter.onNext(snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            });
        });
    }

    public static DocumentListenerService getService() {
        return service;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
    }
}
