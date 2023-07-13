package com.company.userservice.api;

import com.company.userservice.model.User;
import com.company.userservice.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Api(tags ="publicApi")
public class LoginController {
    private final UserService userService;

    @PostMapping
    public String login(@RequestBody User user) {
        return userService.createToken(user);
    }

}
