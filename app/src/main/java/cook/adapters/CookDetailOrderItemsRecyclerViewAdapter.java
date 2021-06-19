package cook.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import cook.ReadyDish;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import tools.Animations;

public class CookDetailOrderItemsRecyclerViewAdapter extends RecyclerView.Adapter<CookDetailOrderItemsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<OrderItem> orderItemsArrayList;
    private TableInfo tableInfo;
    private Consumer<ReadyDish> acceptDishConsumer;

    class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout container_large;
        public ConstraintLayout container;
        public TextView categoryName;
        public TextView name;
        public TextView weight;
        public TextView cost;
        public TextView count;
        public TextView commentary;
        public TextView commentaryTitle;
        public ImageView isReady;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container_large = itemView.findViewById(R.id.order_item_container_large);
            container = itemView.findViewById(R.id.order_item_container);
            categoryName = itemView.findViewById(R.id.category_name);
            name = itemView.findViewById(R.id.dish_name);
            weight = itemView.findViewById(R.id.dish_weight);
            cost = itemView.findViewById(R.id.dish_cost);
            count = itemView.findViewById(R.id.dish_count);
            commentary = itemView.findViewById(R.id.commentary);
            commentaryTitle = itemView.findViewById(R.id.text);
            isReady = itemView.findViewById(R.id.is_ready);
        }
    }

    public void setAcceptedDishConsumer(Consumer<ReadyDish> acceptDishConsumer) {
        this.acceptDishConsumer = acceptDishConsumer;
    }

    public void setOrderItemsData(ArrayList<OrderItem> orderItemsArrayList, TableInfo tableInfo) {
        this.orderItemsArrayList = orderItemsArrayList;
        this.tableInfo = tableInfo;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_detail_order_item, parent, false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.categoryName.setText(orderItemsArrayList.get(position).getCategoryName());
        holder.name.setText(orderItemsArrayList.get(position).getName());
        holder.weight.setText(orderItemsArrayList.get(position).getWeight());
        holder.cost.setText(orderItemsArrayList.get(position).getCost());
        holder.count.setText(orderItemsArrayList.get(position).getCount() + " шт");
        if (orderItemsArrayList.get(position).getCommentary().equals("")) {
            holder.commentaryTitle.setVisibility(View.GONE);
            holder.commentary.setVisibility(View.GONE);
        } else holder.commentary.setText(orderItemsArrayList.get(position).getCommentary() + " ");
        if (orderItemsArrayList.get(position).isReady())
            holder.isReady.setVisibility(View.VISIBLE);
        else holder.isReady.setVisibility(View.GONE);

        Animations.Companion.showView(holder.container_large);
        RxView.clicks(holder.container_large)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                acceptDishConsumer.accept(
                    new ReadyDish(orderItemsArrayList.get(position), tableInfo, position));
            });
        RxView.clicks(holder.container)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                acceptDishConsumer.accept(
                    new ReadyDish(orderItemsArrayList.get(position), tableInfo, position));
            });
    }
    @Override
    public int getItemCount() {
        return orderItemsArrayList.size();
    }
}
