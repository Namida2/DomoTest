package cook;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testfirebase.DeleteOrderObservable;
import com.example.testfirebase.R;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

import interfaces.DeleteOrderInterface;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class CookMainActivity extends AppCompatActivity implements DeleteOrderInterface.Subscriber {

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment navHostFragment;
    private NavHostController navHostController;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DeleteOrderObservable.getObservable().subscribe(this);
        initialisation();

        disposable = getBottomAppBarObservable()
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemId -> {
                NavDestination currentDestination = navHostController.getCurrentDestination();
                switch ( itemId ) {
                    case R.id.cookOrdersFragment:
                        if(currentDestination.getId() != R.id.cookOrdersFragment)
                            navHostController.navigate(R.id.cookOrdersFragment,
                                null
                                //new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build()
                            );
                        break;
                    case R.id.cookChecksFragment:
                        if(currentDestination.getId() != R.id.cookChecksFragment)
                            navHostController.navigate(R.id.cookChecksFragment,
                                null,
                                new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build());
                        break;
                    case R.id.cookProfileFragment:
                        if(currentDestination.getId() != R.id.cookProfileFragment)
                            navHostController.navigate(R.id.cookProfileFragment,
                                null,
                                new NavOptions.Builder().setEnterAnim(R.anim.fragment_show).build());
                        break;
                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showBottomNavigationView();
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
    private void showBottomNavigationView () {
        ViewGroup.LayoutParams layoutParams = bottomNavigationView.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof HideBottomViewOnScrollBehavior) {
                HideBottomViewOnScrollBehavior<BottomNavigationView> hideShowBehavior =
                    (HideBottomViewOnScrollBehavior<BottomNavigationView>) behavior;
                hideShowBehavior.slideUp(bottomNavigationView);
            }
        }
    }

    @Override
    public void deleteOrder(String tableName) {
        showBottomNavigationView();
    }
}

