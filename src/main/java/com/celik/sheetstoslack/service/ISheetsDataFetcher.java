package com.celik.sheetstoslack.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface ISheetsDataFetcher {
    List<List<Object>> fetch(String sheetId, String range) throws IOException, GeneralSecurityException;
}
