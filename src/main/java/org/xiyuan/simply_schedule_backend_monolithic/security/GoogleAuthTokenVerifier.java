package org.xiyuan.simply_schedule_backend_monolithic.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xiyuan.simply_schedule_backend_monolithic.constant.FrontendSource;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Coach;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.Student;
import org.xiyuan.simply_schedule_backend_monolithic.entity.user.User;

import java.util.Arrays;
import java.util.Optional;

@Component
public class GoogleAuthTokenVerifier {
    private final Logger LOG = LoggerFactory.getLogger(GoogleAuthTokenVerifier.class);
    private final NetHttpTransport transport = new NetHttpTransport();
    private final JsonFactory jsonFactory = new GsonFactory();
    @Value("${google_client_id.client}")
    private String CLIENT_ID_CLIENT;
    @Value("${google_client_id.admin}")
    private String CLIENT_ID_ADMIN;

    public Optional<User> verifyGoogleAuthToken(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
//                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                .setAudience(Arrays.asList(CLIENT_ID_CLIENT, CLIENT_ID_ADMIN))
                .build();

        // (Receive idTokenString by HTTPS POST)
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                User user = getUser(idToken);
                return Optional.of(user);
            } else {
                LOG.info("Invalid ID token.");
            }
        } catch (Exception e) {
            LOG.info("Exception in verifyGoogleAuthToken: ", e);
        }
        return Optional.empty();
    }

    private User getUser(GoogleIdToken idToken) {
        Payload payload = idToken.getPayload();
        String audience = (String) payload.getAudience();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");
        User user;
        if (CLIENT_ID_CLIENT.equals(audience)) {
            user = new Student();
            user.setSource(FrontendSource.CLIENT);
        } else {
            user = new Coach();
            user.setSource(FrontendSource.ADMIN);
        }
        user.setEmail(email);
        user.setName(name);
        user.setPicture(picture);
        return user;
    }
}