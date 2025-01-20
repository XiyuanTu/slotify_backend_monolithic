package org.xiyuan.simply_schedule_backend_monolithic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xiyuan.simply_schedule_backend_monolithic.service.StudentService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class OAuth2Controller {

    private final StudentService studentService;

    @Autowired
    public OAuth2Controller(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping(
            value = "/sign-in/google",
            method = RequestMethod.POST,
            produces = {"application/json"}

    )
    public ResponseEntity<Map<String, Object>> createGoogleUser() {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String bearerToken = jwtAuthenticationToken.getToken().getTokenValue();
        try {
            studentService.handleGoogleSignIn(bearerToken);
            return new ResponseEntity<>(Map.of(
                    "token", bearerToken
            ), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", e.getMessage()
            ), HttpStatus.BAD_REQUEST);
        }
    }

}