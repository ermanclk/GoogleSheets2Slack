package com.celik.sheetstoslack.service.impl;

import com.celik.sheetstoslack.model.Member;
import com.celik.sheetstoslack.model.SlackNotification;
import com.celik.sheetstoslack.model.UsersListDTO;
import com.celik.sheetstoslack.service.ISlackClient;
import com.celik.sheetstoslack.model.SlackMessagingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SlackClient implements ISlackClient {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    @Value("${slackAppBotUserToken}")
    private String botUserAuthToken;
    @Value("${slackUsersListUrl}")
    private String listUsersUrl;
    @Value("${slackDirectMessageUrl}")
    private String directMessageUrl;

    Logger logger = LoggerFactory.getLogger(SheetService.class);

    @Override
    public void sendNotificationToIc(SlackNotification notification) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<SlackNotification> request = new HttpEntity<>(notification, createHeaders());
        ResponseEntity<SlackMessagingResponse> response = restTemplate.exchange(
                directMessageUrl, HttpMethod.POST, request, SlackMessagingResponse.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody().isOk()) {
            logger.info("notification send successfully..");
        } else {
            logger.error("error occured while sending notification " + response.getBody());
        }
    }

    @Override
    public Map<String, Member> getSlackTeamRegisteredUsersMap() {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity request = new HttpEntity(createHeaders());
        ResponseEntity<UsersListDTO> response = restTemplate.exchange(
                listUsersUrl, HttpMethod.GET, request, UsersListDTO.class);
        if (response.getStatusCode() != HttpStatus.OK || !response.getBody().isOk()) {
            logger.error("error occured while getting team users " + response.getBody());
            return Collections.emptyMap();
        }
        Map<String, Member> userMap = new HashMap<>();
        response.getBody().getMembers().forEach(member -> userMap.put(member.getProfile().getEmail(), member));
        return userMap;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION_HEADER, BEARER_PREFIX + String.valueOf(botUserAuthToken));
        return headers;
    }
}
