package administrator.interfaces;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import administrator.adapters.UsersRecyclerViewAdapter;
import registration.Employee;

public interface UsersFragmentInterface {
    interface Model {
        FirebaseFirestore getDatabase();
        void setAdapter(UsersRecyclerViewAdapter adapter);
        UsersRecyclerViewAdapter getAdapter();
        void setEmployeesArrayList(List<Employee> employees);
        ArrayList<Employee> getEmployeesArrayList();
        void setView(android.view.View view);
        android.view.View getView();
    }
    interface View {
        void showEmployeeDialog(Employee employee);
        void onComplete();
        void onError(int errorCode);
    }
    interface Presenter {
        void onResume();
        void setModelState();
        android.view.View getView();
        void deleteUser(Employee employee);
        void setView(android.view.View view);
        UsersRecyclerViewAdapter getAdapter();
        void setUserPermission(Employee employee, boolean permission);

    }
}
