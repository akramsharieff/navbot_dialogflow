package com.dialogflowtest.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.*;

@Service
public class DialogFlowService {
	private final SessionsClient sessionsClient;
    private final String project;

    public DialogFlowService(@Value("${dialogflow.project-id}") String project,
                            @Value("${dialogflow.key-file-path}") String keyFilePath) throws IOException {
        this.project = project;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+keyFilePath);
        // Load credentials from the JSON key file
        Credentials credentials = ServiceAccountCredentials.fromStream(
                getClass().getClassLoader().getResourceAsStream(keyFilePath));
        
        // Create a SessionsSettings object with the credentials
        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        
        // Create the SessionsClient with the settings
        this.sessionsClient = SessionsClient.create(sessionsSettings);
    }

    public String detectIntent(String sessionId, String text) {
        SessionName session = SessionName.of(project, sessionId);
        TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("en-US");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
        if (response != null && response.getQueryResult() != null) {
            for (com.google.cloud.dialogflow.v2.Intent.Message message : response.getQueryResult().getFulfillmentMessagesList()) {
                if (message.getText() != null && !message.getText().getText(0).isEmpty()) {
                    return message.getText().getText(0);
                }
            }
        }
        
        // If no fulfillment text is found, return an empty string or handle it as needed.
        return "";
    }
}
