package com.example.testfirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.testfirebase.registration.LoginActivity.TAG;

public class MyService extends Service {

    private static int id = 0;
    private static NotificationChannel channel;
    private static  String channelId = "testFirebase";
    private static  String channelName = "testFirebaseChannel";
    @Override
    public void onCreate() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        createChannel(notificationManager);
        getObservable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe(item -> {
                Notification notification = new NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("title")
                    .setContentText("content")
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .build();
                notificationManager.notify(1, notification);
                Log.d(TAG, item);
            });
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service created");
        return START_STICKY;
    }

    private static Observable<String> getObservable (){
        return Observable.create(emitter -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("new").document("test").addSnapshotListener((snapshot, error) -> {
                if (error != null) {
                    Log.d(TAG, "Error!");
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    emitter.onNext(snapshot.getData().toString());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            });
        });
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
