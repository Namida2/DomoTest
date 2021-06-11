package com.example.testfirebase;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cook.model.DetailOrderActivityModel;
import io.grpc.android.AndroidChannelBuilder;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import static registration.LogInActivity.TAG;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavHostController navHostController;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initialisation();

//        Source source = Source.DEFAULT;
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("items_listener")
//            .document("listener")
//            .get(source).addOnCompleteListener(task -> {
//                if(task.isSuccessful()) {
//                    Map<String, Object> data;
//                    data = task.getResult().getData();
//                    ArrayList<Object> a = (ArrayList<Object>) data.get(DetailOrderActivityModel.FIELD_NOTIFY_NAME);
//                    Map<String, Object> text;
//                    for(int i = 0; i < data.size(); ++i) {
//                        text = (Map<String, Object>) a.get(i);
//                    }
//                    Log.d(TAG, task.getResult().getData().toString());
//                }
//                else Log.d(TAG, task.getException().toString());
//        });

        disposable = getBottomAppBarObservable()
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemId -> {
                NavDestination currentDestination = navHostController.getCurrentDestination();
                switch ( itemId ) {
                    case R.id.tablesFragment:
                        if(currentDestination.getId() != R.id.tablesFragment)
                            navHostController.navigate(R.id.tablesFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                                );
                        break;
                    case R.id.ordersFragment:
                        if(currentDestination.getId() != R.id.ordersFragment)
                            navHostController.navigate(R.id.ordersFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                            );
                        break;
                    case R.id.checkFragment:
                        if(currentDestination.getId() != R.id.checkFragment)
                            navHostController.navigate(R.id.checkFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                            );
                        break;
                    case R.id.profileFragment:
                        if(currentDestination.getId() != R.id.profileFragment)
                            navHostController.navigate(R.id.profileFragment,
                                    null
                                    //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                            );
                        break;
                }
            }, error -> {
                Log.d(TAG, Thread.currentThread().getName());
            });
    }
    private Observable<Integer> getBottomAppBarObservable () {
        return Observable.create(emitter -> {
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                emitter.onNext(item.getItemId());
                return true;
            });
        });
    }
    private void initialisation () {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostController = (NavHostController) navHostFragment.getNavController();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null) disposable.dispose();
    }
}