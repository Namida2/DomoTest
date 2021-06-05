package cook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderItem;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import cook.model.TablesFragmentModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import tools.Pair;

public class CookTablesRecyclerViewAdapter extends RecyclerView.Adapter<CookTablesRecyclerViewAdapter.ViewHolder> {

    private Consumer<String> acceptTableNumber;
    private ArrayList<Pair<ArrayList<OrderItem>, Boolean>> ordersArrayList;
    private ArrayList<String> tableNames;

    public CookTablesRecyclerViewAdapter (Map<String, Pair<ArrayList<OrderItem>, Boolean>> ordersHashMap) {
        Set<String> keys = ordersHashMap.keySet();
        ordersArrayList = new ArrayList<>();
        tableNames = new ArrayList<>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            tableNames.add( name.substring(name.indexOf(TablesFragmentModel.DELIMITER) + 1) );
            ordersArrayList.add(ordersHashMap.get(name));
        }
    }

    public void setAcceptTableNumber (Consumer<String> acceptTableNumber) {
        this.acceptTableNumber = acceptTableNumber;
    }
    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tableNumber;
        public ConstraintLayout container;
        public TextView preview;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tableNumber = itemView.findViewById(R.id.table_number);
            container = itemView.findViewById(R.id.table_container);
            preview = itemView.findViewById(R.id.preview);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String stringPreview = "";
        for(int i = 0; i < Math.min(ordersArrayList.get(position).first.size(), 5); ++i) {
            String name = ordersArrayList.get(position).first.get(i).getName();
            stringPreview += name.length() > 16 ? name.substring(0, 16) + "...\n" : name + "\n" ;
        }
        if (ordersArrayList.get(position).first.size() > 5) stringPreview += "..........";
        holder.preview.setText(stringPreview);
        holder.tableNumber.setText(tableNames.get(position));
        RxView.clicks(holder.container)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {

            });
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cook_order_item, parent, false);
        return new ViewHolder(view);
    }
}
