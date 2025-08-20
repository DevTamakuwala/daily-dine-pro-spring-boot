package io.github.devtamakuwala.dailydine.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Configuration
public class FirebaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public FirebaseApp initialFirebaseApp() throws IOException {

        // When the service account file is placed directly in the 'src/main/resources' directory,
        // it can be accessed by its name without any preceding path.
        // The leading slash is removed to ensure the path is resolved correctly relative to the classpath root.
        ClassPathResource resource = new ClassPathResource("dailydine-ab6dd-firebase-adminsdk-fbsvc-308d0fff7d.json");



        InputStream serviceAccount = resource.getInputStream();


        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();


        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
