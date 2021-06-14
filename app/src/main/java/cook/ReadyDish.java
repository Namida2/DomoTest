package cook;

import com.example.domo.order.OrderItem;
import com.example.domo.order.TableInfo;

public class ReadyDish {

    private OrderItem orderItem;
    private TableInfo tableInfo;
    private int position;

    public ReadyDish (OrderItem orderItem, TableInfo tableInfo, int position) {
        this.orderItem = orderItem;
        this.tableInfo = tableInfo;
        this.position = position;
    }
    public int getPosition() {
        return position;
    }
    public OrderItem getOrderItem() {
        return orderItem;
    }
    public TableInfo getTableInfo() {
        return tableInfo;
    }
}
