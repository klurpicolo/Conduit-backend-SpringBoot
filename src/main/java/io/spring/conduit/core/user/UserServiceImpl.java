package io.spring.conduit.core.user;

import io.spring.conduit.exception.DataDuplicatedException;
import io.spring.conduit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
//        List<User> users = userRepository.findByEmail(email);
//        if(users.isEmpty()){
//            return Optional.empty();
//        } else if(users.size()!=1){
//            throw new DataDuplicatedException();
//        } else {
//            return Optional.of(users.get(0));
//        }
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
//        List<User> users = userRepository.findByUsername(user);
//        if(users.isEmpty()){
//            return Optional.empty();
//        } else if(users.size()!=1){
//            throw new DataDuplicatedException();
//        } else {
//            return Optional.of(users.get(0));
//        }
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}
