package com.celik.sheetstoslack.model;

import com.celik.sheetstoslack.entity.Sheet;

import java.util.List;

public class SheetRow {

    private List<Object> row;

    private int rowOrder;

    private Sheet sheet;

    private String[] triggerColumnsIndex;

    public int getSize() {
        if (this.row != null) {
            return this.row.size();
        }
        return 0;
    }

    public SheetRow(List<Object> row, int rowOrder, Sheet sheet, String[] triggerColumnsIndex) {
        this.row = row;
        this.rowOrder = rowOrder;
        this.sheet = sheet;
        this.triggerColumnsIndex = triggerColumnsIndex;
    }

    public List<Object> getRow() {
        return row;
    }

    public void setRow(List<Object> row) {
        this.row = row;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public String[] getTriggerColumnsIndex() {
        return triggerColumnsIndex;
    }

    public void setTriggerColumnsIndex(String[] triggerColumnsIndex) {
        this.triggerColumnsIndex = triggerColumnsIndex;
    }

    public int getRowOrder() {
        return rowOrder;
    }

    public void setRowOrder(int rowOrder) {
        this.rowOrder = rowOrder;
    }
}
