package com.celik.sheetstoslack;

public class SheetConfigConstant {

    private SheetConfigConstant() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static final String QUERY_RANGE = "data.queryRange";
    public static final String MESSAGE_TEMPLATE = "messageTemplate";
    public static final String TRIGGER_COLUMN_INDEX = "data.triggerColumn.index";
    public static final String SHEET_ID = "sheetId";
    public static final String INDEX_IC_NAME = "data.userName.index";
    public static final String SHEET_NAME = "sheetName";
    public static final String ICNAME_RANGE = "credential.queryRange";
    public static final String USER_NAME_INDEX = "credential.userName.index";
    public static final String USER_MAIL_INDEX = "credential.email.index";

}
