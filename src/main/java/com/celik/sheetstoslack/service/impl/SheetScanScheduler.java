package com.celik.sheetstoslack.service.impl;

import com.celik.sheetstoslack.SheetConfigConstant;
import com.celik.sheetstoslack.entity.Sheet;
import com.celik.sheetstoslack.repository.SheetRepository;
import com.celik.sheetstoslack.service.ISheetService;
import com.celik.sheetstoslack.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
public class SheetScanScheduler {

    private ResourceUtil resourceUtil;
    private ISheetService sheetService;
    private SheetRepository sheetRepository;
    Logger logger = LoggerFactory.getLogger(SheetScanScheduler.class);

    @Scheduled(cron = "${sheet.read.interval}")
    public void initializeScan() throws IOException, GeneralSecurityException {
        try {
            for (Path path : resourceUtil.loadSheetResources()) {
                Map<String, String> sheetConfig = resourceUtil.readSheetConfiguration(path);
                Sheet sheet = getSheet(sheetConfig);
                sheetService.checkForUpdates(sheetConfig, sheet);
            }
        } catch (SocketTimeoutException ex) {
            logger.error("socket timeout exception occured " + ex.getMessage());
        }
    }

    private Sheet getSheet(Map<String, String> sheetConfig) {
        Sheet sheet = sheetRepository.findBySheetId(sheetConfig.get(SheetConfigConstant.SHEET_ID));
        if (sheet == null) {
            sheet = new Sheet();
            sheet.setSheetId(sheetConfig.get(SheetConfigConstant.SHEET_ID));
            sheetRepository.save(sheet);
        }
        return sheet;
    }

    public SheetScanScheduler(ResourceUtil resourceUtil, ISheetService sheetService, SheetRepository sheetRepository) {
        this.resourceUtil = resourceUtil;
        this.sheetService = sheetService;
        this.sheetRepository = sheetRepository;
    }
}
