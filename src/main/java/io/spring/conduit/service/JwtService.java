package io.spring.conduit.service;


import io.spring.conduit.model.User;

import java.util.Optional;

public interface JwtService {
    String toToken(User user);
    Optional<String> getSubFromToken(String token);
}
