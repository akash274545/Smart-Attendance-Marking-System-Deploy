package com.attendance.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${firebase.service.account.path:}")
    private String serviceAccountPath;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder()
                        .setDatabaseUrl(databaseUrl);

                // Try to load service account from file path or use default credentials
                GoogleCredentials credentials = null;
                if (serviceAccountPath != null && !serviceAccountPath.isEmpty()) {
                    try {
                        InputStream serviceAccount = new FileInputStream(serviceAccountPath);
                        credentials = GoogleCredentials.fromStream(serviceAccount);
                        System.out.println("Firebase credentials loaded from file: " + serviceAccountPath);
                    } catch (IOException e) {
                        System.err.println("Warning: Could not load Firebase service account from path: " + serviceAccountPath);
                        System.err.println("Trying to use default credentials or GOOGLE_APPLICATION_CREDENTIALS environment variable.");
                    }
                }
                
                // If no file path provided, try to use default credentials
                if (credentials == null) {
                    try {
                        credentials = GoogleCredentials.getApplicationDefault();
                        System.out.println("Firebase credentials loaded from GOOGLE_APPLICATION_CREDENTIALS environment variable.");
                    } catch (IOException e) {
                        System.err.println("Error: Could not load Firebase credentials.");
                        System.err.println("Please configure Firebase credentials using one of the following methods:");
                        System.err.println("1. Set firebase.service.account.path in application.properties to your service account JSON file path");
                        System.err.println("2. Set GOOGLE_APPLICATION_CREDENTIALS environment variable to your service account JSON file path");
                        throw new RuntimeException("Firebase credentials are required. Please configure firebase.service.account.path in application.properties or set GOOGLE_APPLICATION_CREDENTIALS environment variable.", e);
                    }
                }
                
                // Credentials are required for Firebase Admin SDK
                optionsBuilder.setCredentials(credentials);

                FirebaseOptions options = optionsBuilder.build();
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully with database URL: " + databaseUrl);
            }
        } catch (RuntimeException e) {
            throw e; // Re-throw our custom exceptions
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage(), e);
        }
    }

    @Bean
    public FirebaseDatabase firebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }
}

