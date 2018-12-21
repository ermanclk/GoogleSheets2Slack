package com.celik.sheetstoslack.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class SheetsServiceUtil {

    private SheetsServiceUtil() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    private static final String APPLICATION_NAME = "docs-slack-integration";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/serviceaccount.json";

    public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = getGoogleCredential();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    private static final String WORKING_DIR = "user.dir";

    private static GoogleCredential getGoogleCredential() throws IOException {
        File file = new File( System.getProperty(WORKING_DIR) + "\\config\\serviceaccount.json");
        if (file == null) {
            throw new IllegalStateException("cannot find service acount file...");
        }
        InputStream is = new FileInputStream(file.getPath().toString());
        return GoogleCredential
                .fromStream(new BufferedInputStream(is))
                .createScoped(SCOPES);
    }

}
