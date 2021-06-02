package com.example.testfirebase.order;

public class TableInfo {
    private String tableName;
    private long guestCount;
    private boolean isOrderComplete;

    public TableInfo () {
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setIsComplete(boolean complete) {
        isOrderComplete = complete;
    }
    public void setGuestCount(long guestCount) {
        this.guestCount = guestCount;
    }
    public boolean isOrderComplete() {
        return isOrderComplete;
    }
    public String getTableName() {
        return tableName;
    }
    public long getGuestCount() {
        return guestCount;
    }

}
