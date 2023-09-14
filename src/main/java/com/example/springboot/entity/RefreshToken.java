package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
