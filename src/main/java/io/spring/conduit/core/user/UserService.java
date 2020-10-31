package io.spring.conduit.core.user;

import io.spring.conduit.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String user);
    Optional<User> findById(String id);


}
