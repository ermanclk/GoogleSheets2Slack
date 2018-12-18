package com.crossover.sheetstoslack.service;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class SheetsDataFetcher {

    @Value("${queryRange}")
    private String queryRange;
    @Value("${sheetId}")
    private String spreadsheetId;

    public List<List<Object>> fetch() throws IOException, GeneralSecurityException {

        ValueRange response = GoogleSheetsUtil.getSheetsService().spreadsheets().values()
                .get(spreadsheetId, queryRange)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        } else {
            return values;
        }
    }

}