package com.example.springboot.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "refresh_token")
public class RefreshToken {

    private static final String REFRESH_TOKEN_ID = "refresh_token_id";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String EXPIRED_DATE = "expired_date";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = REFRESH_TOKEN_ID)
    private long refreshTokenId;

    @Column(name = REFRESH_TOKEN, nullable = false, unique = true)
    private String refreshToken;

    @Column(name = EXPIRED_DATE, nullable = false)
    private Instant expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfile userProfile;
}
