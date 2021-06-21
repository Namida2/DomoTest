package com.example.testfirebase.mainActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebase.DeleteOrderObservable;
import com.example.testfirebase.DetailOrderActivity;
import com.example.testfirebase.R;
import com.example.testfirebase.TableMenuBottomSheetDialog;
import com.example.testfirebase.order.TableInfo;
import com.example.testfirebase.services.DocumentDishesListenerService;

import org.jetbrains.annotations.NotNull;

import cook.interfaces.OrdersFragmentInterface;
import cook.model.DetailOrderActivityModel;
import cook.presenters.OrdersFragmentPresenter;
import dialogsTools.AcceptOrCancelDialog;
import dialogsTools.ErrorAlertDialog;
import interfaces.DeleteOrderInterface;
import tools.EmployeeData;

public class OrdersFragment extends Fragment implements OrdersFragmentInterface.View {

    private OrdersFragmentInterface.Presenter presenter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        presenter = new OrdersFragmentPresenter(this);
        getPermission();
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
    public void startDetailOrderActivity(TableInfo tableInfo) {
        if(!getPermission()) return;
        String tableName = DocumentDishesListenerService.TABLE + tableInfo.getTableName();
        if (tableInfo.isOrderComplete()) {
            TableMenuBottomSheetDialog tableMenuBottomSheetDialog = TableMenuBottomSheetDialog.getNewInstance(action -> {
                switch (action) {
                    case TableMenuBottomSheetDialog.ACTION_SHOW_DELETE_ORDER_DIALOG: {
                        AcceptOrCancelDialog acceptOrCancelDialog = new AcceptOrCancelDialog(
                            response -> {
                                if((boolean)response && getPermission())
                                    presenter.deleteOrderFromDatabase(tableInfo.getTableName());
                            }, tableName,
                            getResources().getString(R.string.table_menu_dialog_delete_order_text)
                        );
                        acceptOrCancelDialog.show(getActivity().getSupportFragmentManager(), "");
                    } break;
                    case TableMenuBottomSheetDialog.ACTION_SHOW_DETAIL_ORDER: {
                        Intent intent = new Intent(getContext(), DetailOrderActivity.class);
                        intent.putExtra(DetailOrderActivityModel.EXTRA_TAG, tableInfo.getTableName());
                        getActivity().startActivity(intent);
                    } break;
                }
            },tableName);
            tableMenuBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "");
        }
        else {
            Intent intent = new Intent(getContext(), DetailOrderActivity.class);
            intent.putExtra(DetailOrderActivityModel.EXTRA_TAG, tableInfo.getTableName());
            getActivity().startActivity(intent);
        }
    }

    public boolean getPermission() {
        if(!EmployeeData.permission && !ErrorAlertDialog.isIsExist()) {
            ErrorAlertDialog dialog = ErrorAlertDialog.getNewInstance(ErrorAlertDialog.PERMISSION_ERROR);
            dialog.setActionConsumer(finish -> {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                getActivity().finishAndRemoveTask();
            });
            dialog.show(getActivity().getSupportFragmentManager(), "");
            return false;
        }
        return true;
    }
}
