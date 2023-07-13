package com.company.userservice.service;

import com.company.userservice.entity.UserEntity;
import com.company.userservice.mapper.UserMapper;
import com.company.userservice.model.User;
import com.company.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper mapper;
    private final UserRepository userRepository;

    public String createToken(User user) {
        long now = Instant.now().toEpochMilli();

        if (checkIfUserIsEmpty(user)) {
            return "User not found";
        } else if (checkIfPasswordIsWrong(user)) {
            return "Wrong Password";
        }
        final String token = user.getLogin() + ":" + user.getPassword() + ":" + now;
        log.debug("Token created {}", token);
        return token;
    }

    public boolean tokenIsValid(String token) {
        String[] split = token.split(":");
        String login = split[0];
        String Password = split[1];
        long timeStamp = Long.parseLong(split[2]);
        long now = Instant.now().toEpochMilli();
        User user = User.builder().login(login).password(Password).build();
        if (checkIfUserIsEmpty(user))
            return false;

        boolean tokenIsValid = timeStamp - now < 86400;
        log.debug("Token checked {}", tokenIsValid);
        return tokenIsValid;
    }

    public boolean checkIfUserIsEmpty(User user) {
        Optional<User> userCheck = findByLogin(user.getLogin());
        boolean empty = userCheck.isEmpty();
        log.debug("User is empty: {}", empty);
        return empty;
    }

    public boolean checkIfPasswordIsWrong(User user) {
        Optional<User> userCheck = findByLogin(user.getLogin());
        if (userCheck.isEmpty()) return true;

        User newUserCheck = userCheck.get();
        boolean passwordIsWrong = !newUserCheck.getPassword().equals(user.getPassword());
        log.debug("Password is wrong: {}", passwordIsWrong);
        return passwordIsWrong;
    }

    public User create(User user) {
        var saved = mapper.map(userRepository.save(mapper.map(user)));
        log.debug("User saved to db, user: {}", saved.getId());
        return saved;
    }

    public User update(User user, Integer id) {
        var userToUpdate = userRepository.findById(id).map(mapper::map)
                .orElseThrow(() -> new IllegalStateException("user with id " + id + "does not exist"));
        if (user.getLogin() != null) {
            userToUpdate = userToUpdate.toBuilder().login(user.getLogin()).build();
        }
        if (user.getPassword() != null) {
            userToUpdate = userToUpdate.toBuilder().password(user.getPassword()).build();
        }
        var updated = mapper.map(userRepository.save(mapper.map(userToUpdate)));
        log.debug("User updated, user: {}", updated.getId());
        return updated;
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
        log.debug("User deleted, user: {}", id);
    }

    public Optional<User> findById(int id) {
        final Optional<User> user = userRepository.findById(id).map(mapper::map);
        log.debug("User found, user: {}", user);
        return user;
    }

    public Optional<User> findByLogin(String login) {
        final Optional<User> user = userRepository.findByLogin(login).map(mapper::map);
        log.debug("User found, user: {}", user);
        return user;
    }

    public List<User> findAllUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userRepository.findAll().forEach(userEntities::add);
        log.debug("All users found");
        return userEntities.stream().map(mapper::map).collect(Collectors.toList());
    }

}