package com.celik.sheetstoslack.service.impl;

import com.celik.sheetstoslack.SheetConfigConstant;
import com.celik.sheetstoslack.entity.NotificationColumn;
import com.celik.sheetstoslack.entity.Sheet;
import com.celik.sheetstoslack.model.SheetRow;
import com.celik.sheetstoslack.model.SlackNotification;
import com.celik.sheetstoslack.repository.NotificationColumnRepository;
import com.celik.sheetstoslack.service.ISheetService;
import com.celik.sheetstoslack.service.ISheetsDataFetcher;
import com.celik.sheetstoslack.service.ISlackClient;
import com.celik.sheetstoslack.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SheetService implements ISheetService {

    private static final String INDEX = "index";
    private static final String DATA = "data";
    private static final String EMPTY_STRING = "";
    private static final String EMPTY_COLUMN = "<EMPTY>";
    private static final String DOT = ".";
    Logger logger = LoggerFactory.getLogger(SheetService.class);
    private ISheetsDataFetcher sheetsDataFetcher;
    private ISlackClient slackClient;
    private NotificationColumnRepository columnRepository;

    private IUserInfoService userInfoService;

    @Override
    @Async
    public void checkForUpdates(Map<String, String> sheetConfig, Sheet sheet) throws IOException,
            GeneralSecurityException {

        List<List<Object>> dataSheet = sheetsDataFetcher.fetch(sheet.getSheetId(), sheetConfig
                .get(SheetConfigConstant.QUERY_RANGE));
        String[] triggerColumnsIndex = sheetConfig.get(SheetConfigConstant.TRIGGER_COLUMN_INDEX).split(",");
        int rowOrder = 0;
        for (List<Object> row : dataSheet) {
            rowOrder++;
            SheetRow sheetRow = new SheetRow(row, rowOrder, sheet, triggerColumnsIndex);
            Map<Integer, NotificationColumn> changeMap = getRowUpdates(sheetRow);
            if (!changeMap.isEmpty()) {
                String message = messageParser(sheetConfig, row, changeMap);
                String userName = String.valueOf(row.get(Integer.parseInt(sheetConfig.get(SheetConfigConstant.INDEX_IC_NAME))));
                String channelName = userInfoService.getSlackChannelForUser(userName, sheetConfig);
                SlackNotification notification = new SlackNotification(sheetConfig.get(SheetConfigConstant.SHEET_NAME), message,
                        userName, channelName);
                slackClient.sendNotificationToIc(notification);
            }
        }
    }

    private static String getRegexForKey(String key) {
        key = key.replace(DATA, EMPTY_STRING).replace(INDEX, EMPTY_STRING).replace(DOT, EMPTY_STRING);
        return "\\{" + key + "\\}";
    }

    private Map<Integer, NotificationColumn> getRowUpdates(SheetRow sheetRow) {
        Map<Integer, NotificationColumn> changeMap = new HashMap<>();

        try {
            for (String index : sheetRow.getTriggerColumnsIndex()) {
                String rowValue = "";
                if (sheetRow.getSize() > Integer.parseInt(index)) {
                    rowValue = (String) sheetRow.getRow().get(Integer.parseInt(index));
                }
                NotificationColumn column = columnRepository.findBySheetAndAndRowNumberAndIndex(sheetRow.getSheet(),
                        sheetRow.getRowOrder(),
                        Integer.parseInt(index));
                if (column == null) {
                    column = new NotificationColumn();
                    column.setValue(rowValue);
                    column.setSheet(sheetRow.getSheet());
                    column.setRowNumber(sheetRow.getRowOrder());
                    column.setIndex(Integer.parseInt(index));
                }
                if (column != null && !column.getValue().equals(rowValue)) {
                    column.setOldValue(column.getValue());
                    column.setValue(rowValue);
                    changeMap.put(Integer.parseInt(index), column);
                }
                columnRepository.save(column);

            }
        } catch (Exception ex) {
            logger.error("error occured while estimating updates on sheet " + ex.getMessage());
        }

        return changeMap;
    }

    private static String messageParser(Map<String, String> sheetConfig, List<Object> row, Map<Integer,
            NotificationColumn> changeMap) {

        String message = sheetConfig.get(SheetConfigConstant.MESSAGE_TEMPLATE);

        for (Map.Entry<String, String> entry : sheetConfig.entrySet()) {

            if (isColumnIndexEntry(entry)) {
                String regex = getRegexForKey(entry.getKey());
                String value = getConfiguredColumnValue(sheetConfig, row, entry);
                if (!entry.getValue().contains(",") && changeMap.containsKey(Integer.parseInt(entry.getValue()))) {
                    NotificationColumn column = changeMap.get(Integer.parseInt(entry.getValue()));
                    value = "( old value: " + column.getOldValue() + ", new value : " + column.getValue() + " )";
                }
                if (value.equals(EMPTY_STRING)) {
                    value = EMPTY_COLUMN;
                }
                message = message.replaceAll(regex, value);
            }
        }
        return message;
    }

    private static String getConfiguredColumnValue(Map<String, String> sheetConfig, List<Object> row, Map
            .Entry<String, String> entry) {
        String configColumnIndex = sheetConfig.get(entry.getKey());
        if (configColumnIndex.contains(",")) {
            return EMPTY_STRING;
        }
        if (Integer.parseInt(configColumnIndex) >= row.size()) {
            return EMPTY_STRING;
        }
        return String.valueOf(row.get(Integer.parseInt(configColumnIndex)));
    }

    private static boolean isColumnIndexEntry(Map.Entry<String, String> entry) {
        return entry.getKey().endsWith(INDEX) && entry.getKey().startsWith(DATA);
    }

    public SheetService(ISheetsDataFetcher sheetsDataFetcher, ISlackClient slackClient, NotificationColumnRepository
            columnRepository, IUserInfoService userInfoService) {
        this.sheetsDataFetcher = sheetsDataFetcher;
        this.slackClient = slackClient;
        this.columnRepository = columnRepository;
        this.userInfoService = userInfoService;
    }
}
