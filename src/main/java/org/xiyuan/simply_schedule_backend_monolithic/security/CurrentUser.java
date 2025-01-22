package org.xiyuan.simply_schedule_backend_monolithic.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "@userServiceImpl.getUserFromJwt(#this)")
public @interface CurrentUser {
}
