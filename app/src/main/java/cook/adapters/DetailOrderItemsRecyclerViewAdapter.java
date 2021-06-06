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

public class DetailOrderItemsRecyclerViewAdapter extends RecyclerView.Adapter<DetailOrderItemsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<OrderItem> orderItemsArrayList;

    class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout container;
        public TextView categoryName;
        public TextView name;
        public TextView weight;
        public TextView cost;
        public TextView count;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
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
        View view = layoutInflater.inflate(R.layout.activity_detail_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
