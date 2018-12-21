package com.celik.sheetstoslack.service;

import com.celik.sheetstoslack.entity.Sheet;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface ISheetService {
    @Async
    void checkForUpdates(Map<String, String> sheetConfig, Sheet sheet) throws IOException,
            GeneralSecurityException;
}
