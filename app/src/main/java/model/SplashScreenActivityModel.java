package model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import interfaces.SplashScreenInterface;

public class SplashScreenActivityModel implements SplashScreenInterface.Model {

    public static final String COLLECTION_EMPLOYEES_NAME = "employees";
    public static final String EMPLOYEES_FIELD_POST = "post";

    public static final String COLLECTION_LISTENERS_NAME = "listeners";
    public static final String DOCUMENT_DISHES_LISTENER_NAME = "dishes_listener";
    public static final String DOCUMENT_ORDERS_LISTENER_NAME = "orders_listener";
    public static final String FIELD_TABLE_NAME = "tableName";
    public static final String FIELD_FIELD_TABLE_NAME = "tableName";

    public static final String COOK_POST_NAME = "Повар";
    public static final String WAITER_POST_NAME = "Официант";
    public static final String ADMINISTRATOR_POST_NAME = "Администратор";

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public SplashScreenActivityModel () {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }
    @Override
    public FirebaseFirestore getDatabase() {
        return db;
    }
    @Override
    public FirebaseAuth gerAuth() {
        return auth;
    }
}
