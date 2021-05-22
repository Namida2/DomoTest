package model;

import com.example.testfirebase.order.Dish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import interfaces.OrderActivityInterface;

public class MenuDialogModel implements OrderActivityInterface.MenuDialog.Model {

    private Map<String, ArrayList<Dish>> dishesHashMap = new HashMap<>();

    public Map<String, ArrayList<Dish>> getDishesHashMap() {
        return dishesHashMap;
    }
}
