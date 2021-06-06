package cook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.example.testfirebase.order.OrderItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import tools.Animations;

public class DetailOrderItemsRecyclerViewAdapter extends RecyclerView.Adapter<DetailOrderItemsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<OrderItem> orderItemsArrayList;

    class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout container;
        public TextView categoryName;
        public TextView name;
        public TextView weight;
        public TextView cost;
        public TextView count;
        public TextView commentary;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.order_item_container_large);
            categoryName = itemView.findViewById(R.id.category_name);
            name = itemView.findViewById(R.id.dish_name);
            weight = itemView.findViewById(R.id.dish_weight);
            cost = itemView.findViewById(R.id.dish_cost);
            count = itemView.findViewById(R.id.dish_count);
            commentary = itemView.findViewById(R.id.commentary);
        }
    }
    public void setOrderItemsArrayList (ArrayList<OrderItem> orderItemsArrayList) {
        this.orderItemsArrayList = orderItemsArrayList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_detail_order_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.categoryName.setText(orderItemsArrayList.get(position).getCategoryName());
        holder.name.setText(orderItemsArrayList.get(position).getName());
        holder.weight.setText(orderItemsArrayList.get(position).getWeight());
        holder.cost.setText(orderItemsArrayList.get(position).getCost());
        holder.count.setText(orderItemsArrayList.get(position).getCount() + " шт");
        holder.commentary.setText(orderItemsArrayList.get(position).getCommentary() + " ");
        Animations.Companion.startAnimationViewShowing(holder.container);

    }

    @Override
    public int getItemCount() {
        return orderItemsArrayList.size();
    }
}
