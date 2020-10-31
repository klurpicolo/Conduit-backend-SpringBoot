package io.spring.conduit.core.jwt;

import io.spring.conduit.model.User;

import java.util.Optional;

public interface JwtService {

    String getUserToken(User user);
    Optional<String> getSubjectFromToken(String token);
}
