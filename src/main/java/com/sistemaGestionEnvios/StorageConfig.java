package com.sistemaGestionEnvios;
 
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
 
@Configuration
public class StorageConfig {
 
    @Value("${firebase.json.path}")
    private String jsonPath;
 
    @Value("${firebase.json.file}")
    private String jsonFile;
 
    @Bean
    public Storage storage() throws IOException {
        File secretFile = new File("/etc/secrets/" + jsonFile);
        InputStream inputStream;

        if (secretFile.exists()) {
            inputStream = new FileInputStream(secretFile);
        } else {
            ClassPathResource resource =
                    new ClassPathResource(jsonPath + File.separator + jsonFile);
            inputStream = resource.getInputStream();
        }

        try (inputStream) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        }
    }  
}