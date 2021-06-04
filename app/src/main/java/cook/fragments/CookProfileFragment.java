package cook.fragments;

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

import org.jetbrains.annotations.NotNull;

public class CookProfileFragment extends Fragment {

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        //View contentView = presenter.getView();
        //if(contentView != null) return contentView;
        //contentView = inflater.inflate(R.layout.fragment_tables, container, false);
        //RecyclerView recyclerView = contentView.findViewById(R.id.tables_recycle_view);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //recyclerView.setAdapter(presenter.getAdapter());
        //presenter.setModelState(contentView);
        //return contentView;
        super.onCreate(savedInstanceState);
    }

}
