package com.celik.sheetstoslack.service;

import com.celik.sheetstoslack.model.Member;
import com.celik.sheetstoslack.model.SlackNotification;

import java.util.Map;

public interface ISlackClient {
    void sendNotificationToIc(SlackNotification notification);

    Map<String, Member> getSlackTeamRegisteredUsersMap();
}
