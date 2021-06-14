package com.example.testfirebase.order;

import androidx.annotation.Nullable;

public class TableInfo {
    private String tableName;
    private long guestCount;
    private boolean isOrderComplete;

    public TableInfo () { }
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

    @Override
    public int hashCode() {
        return tableName.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null || obj.getClass() != TableInfo.class) return false;
        TableInfo tableInfo = (TableInfo) obj;
        return tableInfo.getTableName().equals(this.tableName)
            && tableInfo.getGuestCount() == this.guestCount
            && tableInfo.isOrderComplete == this.isOrderComplete;
    }
}
