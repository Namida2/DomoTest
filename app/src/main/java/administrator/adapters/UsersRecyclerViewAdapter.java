package administrator.adapters;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.R;
import com.jakewharton.rxbinding4.view.RxView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import registration.Employee;
import tools.Animations;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Employee> employees = new ArrayList<>();
    private Consumer<Employee> acceptEmployee;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout largeContainer;
        public ConstraintLayout container;
        public TextView name;
        public TextView email;
        public TextView post;
        public TextView text1;
        public TextView text2;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.user_info_constraint_layout);
            largeContainer = itemView.findViewById(R.id.user_item_container_large);
            name = itemView.findViewById(R.id.user_name_text_view);
            email = itemView.findViewById(R.id.user_email_text_view);
            post = itemView.findViewById(R.id.user_post_text_view);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
        }
    }
    public void setActionsConsumer(Consumer<Employee> acceptEmployee ) {
        this.acceptEmployee = acceptEmployee;
    }
    public void setEmployeesArrayList(ArrayList<Employee> employees) {
        this.employees = employees;
        this.notifyDataSetChanged();
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_users_item, parent, false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.name.setText(employees.get(position).getName());
        holder.email.setText(employees.get(position).getEmail());
        holder.post.setText(employees.get(position).getPost());
        holder.text1.setVisibility(View.GONE);
        holder.text2.setVisibility(View.GONE);
        if(employees.get(position).getPermission()) {
            holder.text1.setVisibility(View.VISIBLE);
            holder.text2.setVisibility(View.VISIBLE);
        }
        RxView.clicks(holder.largeContainer)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                acceptEmployee.accept(employees.get(position));
            });

        RxView.clicks(holder.container)
            .debounce(150, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(unit -> {
                acceptEmployee.accept(employees.get(position));
            });

        Animations.Companion.showView(holder.largeContainer);
    }
    @Override
    public int getItemCount() {
        return employees.size();
    }
}
