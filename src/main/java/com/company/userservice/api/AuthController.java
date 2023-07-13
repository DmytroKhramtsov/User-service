package com.company.userservice.api;

import com.company.userservice.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags ="internalApi")
public class AuthController {
    private final UserService userService;

    @GetMapping("/token")
    public boolean checkToken(@RequestParam("token") String token) {
        return userService.tokenIsValid(token);
    }

}
