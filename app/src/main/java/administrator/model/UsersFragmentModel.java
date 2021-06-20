package administrator.model;

import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import administrator.adapters.UsersRecyclerViewAdapter;
import administrator.interfaces.UsersFragmentInterface;
import registration.Employee;

public class UsersFragmentModel implements UsersFragmentInterface.Model {

    private View view;
    private FirebaseFirestore db;
    private ArrayList<Employee> employees;
    private UsersRecyclerViewAdapter adapter;
    public static final String DOCUMENT_EMPLOYEES_LISTENER = "employees_listener";
    public static final String FIELD_EMPLOYEE = "employee";

    public UsersFragmentModel () {
        this.db = FirebaseFirestore.getInstance();
        employees = new ArrayList<>();
    }

    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }

    @Override
    public void setAdapter(UsersRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public UsersRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setEmployeesArrayList(List<Employee> employees) {
        this.employees.clear();
        this.employees.addAll(employees);
    }

    @Override
    public ArrayList<Employee> getEmployeesArrayList() {
        return employees;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public View getView() {
        return view;
    }
}
