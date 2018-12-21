package com.celik.sheetstoslack.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface IUserInfoService {
    String getSlackChannelForUser(String userName, Map<String, String> configMap) throws IOException,
            GeneralSecurityException;
}
