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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CookOrdersRecyclerViewAdapter extends RecyclerView.Adapter<CookOrdersRecyclerViewAdapter.ViewHolder> {


    private ArrayList<OrderItem> orderItemsArrayList;

    public CookOrdersRecyclerViewAdapter () {

    }

    @Override
    public int getItemCount() {
        return orderItemsArrayList.size();
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

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_cook_orders, parent, false);
        return new ViewHolder(view);
    }
}
