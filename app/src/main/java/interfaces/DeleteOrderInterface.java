package interfaces;

public interface DeleteOrderInterface {
    interface Observable {
        void notifySubscribers(String tableName);
        void subscribe(DeleteOrderInterface.Subscriber subscriber);
        void unSubscribe(DeleteOrderInterface.Subscriber subscriber);
    }
    interface Subscriber {
        void deleteOrder(String tableName);
    }
}
