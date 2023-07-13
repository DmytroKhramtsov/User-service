package com.company.userservice.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder(toBuilder = true)
public class User {
    Integer id;
    String login;
    String password;
}
