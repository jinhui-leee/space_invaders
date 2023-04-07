package org.newdawn.spaceinvaders;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class Firebase {

    public static void initialize() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("/resources/key.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://source-code-analysis.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
    }
}
