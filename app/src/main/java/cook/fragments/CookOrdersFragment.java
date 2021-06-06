package cook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

import cook.DetailOrderActivity;
import cook.interfaces.TablesFragmentInterface;
import cook.model.DetailOrderActivityModel;
import cook.presenters.TablesFragmentPresenter;

public class CookOrdersFragment extends Fragment implements TablesFragmentInterface.View  {

    private TablesFragmentInterface.Presenter presenter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        presenter = new TablesFragmentPresenter(this);
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View contentView = presenter.getView();
        if(contentView != null) return contentView;
        contentView = inflater.inflate(R.layout.fragment_cook_orders, container, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.orders_recycler_view);
        recyclerView.setAdapter(presenter.getAdapter());
        presenter.setModelState(contentView);
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void startDetailOrderActivity(String tableNumber) {
        Intent intent = new Intent(getContext(), DetailOrderActivity.class);
        intent.putExtra(DetailOrderActivityModel.EXTRA_TAG, tableNumber);
        getActivity().startActivity(intent);
    }
}
