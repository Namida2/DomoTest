package model;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.adapters.OrderRecyclerViewAdapter;
import com.example.testfirebase.order.Dish;

import java.util.ArrayList;
import java.util.function.Consumer;

import interfaces.OrderActivityInterface;
import tools.Pair;

public class OrderActivityModel implements OrderActivityInterface.Model {

    private ArrayList<Pair<Dish, Pair<String, Integer>>> orderItemArrayList;

    private RecyclerView orderRecyclerView;
    private OrderRecyclerViewAdapter adapter;

    @Override
    public Consumer<Pair<Dish, Pair<String, Integer>>> getNotifyOrderAdapterConsumer() {
        return null;
    }
}
