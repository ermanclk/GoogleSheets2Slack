package com.celik.sheetstoslack.service.impl;

import com.celik.sheetstoslack.service.ISheetsDataFetcher;
import com.celik.sheetstoslack.util.SheetsServiceUtil;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class SheetsDataFetcher implements ISheetsDataFetcher {

    @Override
    public List<List<Object>> fetch(String sheetId, String range) throws IOException, GeneralSecurityException {

        ValueRange response = SheetsServiceUtil.getSheetsService().spreadsheets().values()
                .get(sheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        } else {
            return values;
        }
    }
}