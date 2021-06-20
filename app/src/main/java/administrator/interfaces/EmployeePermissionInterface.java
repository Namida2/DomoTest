package administrator.interfaces;

public interface EmployeePermissionInterface {
    interface Observable {
        void notifySubscribers(String permission);
        void subscribe(EmployeePermissionInterface.Subscriber subscriber);
        void unSubscribe(EmployeePermissionInterface.Subscriber subscriber);
    }
    interface Subscriber {
        void acceptPermission(boolean permission);
    }
}
