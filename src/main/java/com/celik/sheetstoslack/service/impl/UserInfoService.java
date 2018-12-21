package com.celik.sheetstoslack.service.impl;

import com.celik.sheetstoslack.SheetConfigConstant;
import com.celik.sheetstoslack.model.Member;
import com.celik.sheetstoslack.service.ISheetsDataFetcher;
import com.celik.sheetstoslack.service.IUserInfoService;
import com.celik.sheetstoslack.service.ISlackClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserInfoService implements IUserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    private ISheetsDataFetcher sheetsDataFetcher;
    private ISlackClient slackClient;
    private static final ConcurrentHashMap<String, Map<String, String>> sheetUsersMap=new ConcurrentHashMap<>();
    public UserInfoService(ISheetsDataFetcher sheetsDataFetcher, ISlackClient slackClient) {
        this.sheetsDataFetcher = sheetsDataFetcher;
        this.slackClient = slackClient;
    }

    private Map<String, String> getUserMap(Map<String, String> configMap) throws IOException, GeneralSecurityException {
        String sheetId = configMap.get(SheetConfigConstant.SHEET_ID);
        Map<String, String> teamMap = sheetUsersMap.get(sheetId);
        if (teamMap == null) {
            teamMap = fetchUserListFromSheet(configMap);
            sheetUsersMap.put(sheetId, teamMap);
        }
        return teamMap;
    }

    private Map<String, String> fetchUserListFromSheet(Map<String, String> configMap) throws IOException,
            GeneralSecurityException {
        int userIndex = Integer.parseInt(configMap.get(SheetConfigConstant.USER_NAME_INDEX));
        int emailIndex = Integer.parseInt(configMap.get(SheetConfigConstant.USER_MAIL_INDEX));
        Map<String, String> userMap = new HashMap<>();
        List<List<Object>> slackNamesList = sheetsDataFetcher.fetch(configMap.get(SheetConfigConstant.SHEET_ID), configMap.get
                (SheetConfigConstant.ICNAME_RANGE));
        for (List<Object> slackNames : slackNamesList) {
            userMap.put(slackNames.get(userIndex).toString().toLowerCase(), slackNames.get(emailIndex).toString());
        }
        return userMap;
    }

    @Override
    public String getSlackChannelForUser(String userName, Map<String, String> configMap) throws IOException,
            GeneralSecurityException {
        String slackName = getUserMap(configMap).get(userName.toLowerCase());
        if (slackName == null) {
            throw new IllegalArgumentException("cannot find email information for user" + userName);
        }

        Map<String, Member> slackTeamUsersMap = slackClient.getSlackTeamRegisteredUsersMap();
        if (slackTeamUsersMap.get(slackName) == null) {
            logger.info("cannot find user with slack id, active users in slack team: " + slackTeamUsersMap.keySet()
                    .toString());
            throw new IllegalArgumentException("can not find user with slack name: " + slackName);
        }
        return slackTeamUsersMap.get(slackName).getId();
    }



}
