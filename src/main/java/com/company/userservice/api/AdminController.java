package com.company.userservice.api;

import com.company.userservice.model.User;
import com.company.userservice.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags ="adminApi")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user){
        return userService.create(user);
    }

    @PutMapping(path = "{id}")
    public User update(@RequestBody User user, @PathVariable("id") Integer id) {
        return userService.update(user, id);
    }

    @DeleteMapping("/delete/id")
    public void deleteById(@RequestParam("userId") Integer id) {
        userService.deleteById(id);
    }

    @GetMapping("/search/id")
    public Optional<User> findById(@RequestParam("userId") Integer userId) {
        return userService.findById(userId);
    }

    @GetMapping("/search/login")
    public Optional<User> findByLogin(@RequestParam("login") String login) {
        return userService.findByLogin(login);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }
}
