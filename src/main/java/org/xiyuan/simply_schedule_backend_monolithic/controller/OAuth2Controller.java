package org.xiyuan.simply_schedule_backend_monolithic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xiyuan.simply_schedule_backend_monolithic.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class OAuth2Controller {

    private final UserService userService;

    @Autowired
    public OAuth2Controller(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
            value = "/sign-in/google",
            method = RequestMethod.POST,
            produces = {"application/json"}
    )
    public ResponseEntity<Map<String, Object>> createGoogleUser(@AuthenticationPrincipal Jwt jwt) {
        try {
            userService.handleGoogleSignIn(jwt);
            return new ResponseEntity<>(Map.of(
                    "token", jwt.getTokenValue()
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }
}