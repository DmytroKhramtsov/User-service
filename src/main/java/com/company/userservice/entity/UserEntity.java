package com.company.userservice.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table(name = "users")
public class UserEntity {
    @Id
    @Column("id")
    Integer id;
    @Column("login")
    String login;
    @Column("password")
    String password;
}
