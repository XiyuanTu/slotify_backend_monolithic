package org.xiyuan.simply_schedule_backend_monolithic.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xiyuan.simply_schedule_backend_monolithic.entity.Student;

import java.util.Collections;
import java.util.Optional;

@Component
public class ValidateGoogleAuthToken {
    private static final Logger LOG = LoggerFactory.getLogger(ValidateGoogleAuthToken.class);
    private static final String CLIENT_ID = "692439382840-504o7a0lrmdf94htqgh2fivar19rj13d.apps.googleusercontent.com";

    private static final NetHttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new GsonFactory();

    public static Optional<Student> verifyGoogleAuthToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        // (Receive idTokenString by HTTPS POST)
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");

                Student student = new Student();
                student.setEmail(email);
                student.setName(name);
                student.setPicture(picture);

                return Optional.of(student);
            } else {
                LOG.info("Invalid ID token.");
            }
        } catch (Exception e) {
            LOG.info("Exception in verifyGoogleAuthToken: ", e);
        }
        return Optional.empty();
    }
}