package io.spring.conduit.helper;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String id() default "mockId";
    String email() default "mockEmail@gmail.com";
    String username();
    String password() default "mockPassword";
}
