package cook.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderItem;
import com.example.testfirebase.order.TableInfo;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import cook.model.OrdersFragmentModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import model.OrderActivityModel;
import tools.Animations;

public class CookOrdersRecyclerViewAdapter extends RecyclerView.Adapter<CookOrdersRecyclerViewAdapter.ViewHolder> {

    private Consumer<TableInfo> acceptOrderArrayList;
    private ArrayList<ArrayList<OrderItem>> ordersArrayList;
    private ArrayList<String> tableNumbers;
    private ArrayList<TableInfo> myTableInfoArrayList;

    public void setOrdersArrayList(Map<String, ArrayList<OrderItem>> ordersHashMap, ArrayList<TableInfo> tableInfoArrayList) {
        //if(ordersHashMap == null) return;
        Set<String> keys = ordersHashMap.keySet();
        ordersArrayList = new ArrayList<>();
        tableNumbers = new ArrayList<>();
        myTableInfoArrayList = new ArrayList<>();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            tableNumbers.add( name.substring(name.indexOf(OrdersFragmentModel.DELIMITER) + 1) );
            ordersArrayList.add(ordersHashMap.get(name));
            for(int i = 0; i < tableInfoArrayList.size(); ++i) {
                if(tableInfoArrayList.get(i).getTableName().equals(name)) {
                    myTableInfoArrayList.add(tableInfoArrayList.get(i));
                    break;
                }
            }
        }
        this.notifyDataSetChanged();
    }
    public void setAcceptOrderArrayListConsumer(Consumer<TableInfo> acceptOrderArrayList) {
        this.acceptOrderArrayList = acceptOrderArrayList;
    }
    @Override
    public int getItemCount() {
        return ordersArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout containerLarge;
        public TextView completeStatusTextView;
        public TextView tableNumber;
        public ConstraintLayout container;
        public TextView preview;
        public TextView guestCount;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            containerLarge = itemView.findViewById(R.id.order_item_container_large);
            completeStatusTextView = itemView.findViewById(R.id.complete_text_view);
            container = itemView.findViewById(R.id.table_container);
            tableNumber = itemView.findViewById(R.id.table_number);
            preview = itemView.findViewById(R.id.preview);
            guestCount = itemView.findViewById(R.id.guests_count);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        StringBuilder stringPreview = new StringBuilder();
        for(int i = 0; i < Math.min(ordersArrayList.get(position).size(), 5); ++i) {
            String name = ordersArrayList.get(position).get(i).getName();
            stringPreview.append(name.length() > 16 ? name.substring(0, 16) + "...\n" : name + "\n");
        }
        holder.completeStatusTextView.setVisibility(View.GONE);
        Map<String, ArrayList<OrderItem>> ooooo = new OrderActivityModel().getNotEmptyTablesOrdersHashMap();
        if(allDishesReady(ordersArrayList.get(position)))
            holder.completeStatusTextView.setVisibility(View.VISIBLE);
        holder.preview.setText(stringPreview.toString());
        holder.tableNumber.setText(tableNumbers.get(position));
        RxView.clicks(holder.container)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableNumbers.get(position));
                tableInfo.setIsComplete(allDishesReady(ordersArrayList.get(position)));
                acceptOrderArrayList.accept(tableInfo);
            });
        holder.guestCount.setText(Integer.toString((int) myTableInfoArrayList.get(position).getGuestCount()));
        Animations.Companion.showView(holder.containerLarge);
    }
    private boolean allDishesReady (ArrayList<OrderItem> orderItems) {
        boolean allReady = true;
        for (OrderItem orderItem : orderItems) {
            if (!orderItem.isReady()) {
                allReady = false;
                break;
            }
        }
        return allReady;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cook_order_item, parent, false);
        return new ViewHolder(view);
    }

    public void notifyTable(String notifiableTableNumber) {
        notifiableTableNumber = notifiableTableNumber
            .substring(notifiableTableNumber.indexOf(OrdersFragmentModel.DELIMITER) + 1);
        for(int i = 0; i < tableNumbers.size(); ++i) {
            if(tableNumbers.get(i).equals(notifiableTableNumber)) {
                this.notifyItemChanged(i);
                break;
            }
        }
    }
}
