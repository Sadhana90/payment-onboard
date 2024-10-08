package com.paymentonboard.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_token_info")
@Getter
@Setter
public class JwtTokenInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    private Boolean isActive;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}