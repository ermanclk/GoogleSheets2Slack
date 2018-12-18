package com.crossover.sheetstoslack.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class SheetService {

    Logger logger = LoggerFactory.getLogger(SheetService.class);
    private SheetsDataFetcher sheetsDataFetcher;

    @Scheduled(cron = "${interval}")
    public void getSheetContent() throws IOException, GeneralSecurityException {

        List<List<Object>> rows = sheetsDataFetcher.fetch();
        for (List row : rows) {
            logger.info("name : "+row.get(0)+", target "+row.get(1) +", jira "+ row.get(2) +"  pr "+row.get(3)+"" +
                            ", reviewer "+row.get(4)+", status "+ row.get(5)+" \n");

        }

    }

    public SheetService(SheetsDataFetcher sheetsDataFetcher) {
        this.sheetsDataFetcher = sheetsDataFetcher;
    }
}
