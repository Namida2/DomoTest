package administrator.interfaces;

public interface EmployeePermissionInterface {
    interface Observable {
        void notifySubscribers();
        void subscribe(EmployeePermissionInterface.Subscriber subscriber);
        void unSubscribe(EmployeePermissionInterface.Subscriber subscriber);
    }
    interface Subscriber {
        boolean acceptPermission(String employeeEmail, boolean permission);
    }
}
