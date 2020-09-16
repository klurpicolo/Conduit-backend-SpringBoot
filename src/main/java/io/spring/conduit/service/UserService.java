package io.spring.conduit.service;

import io.spring.conduit.model.User;

import java.util.Optional;

public interface UserService{
    User createUser (User user);
    Optional<User> findById (String id);
}
