package com.example.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String USER_ID = "user_id";
    private static final String LOGIN_NAME = "login_name";
    private static final String HASH_PASSWORD = "hash_password";
    private static final String DISPLAY_NAME = "display_name";
    private static final String EMAIL_ADDRESS = "email_address";
    private static final String NEW_EMAIL_ADDRESS = "new_email_address";
    private static final String EMAIL_ADDRESS_VERIFIED = "email_address_verified";
    private static final String VERIFY_CODE = "verify_code";
    private static final String VERIFY_EXPIRED_CODE_TIME = "verify_expired_code_time";
    private static final String RESET_PASSWORD_CODE = "reset_password_code";
    private static final String RESET_PASSWORD_EXPIRED_CODE_TIME = "reset_password_expired_code_time";
    private static final String ROLES = "roles";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = USER_ID)
    private Long userID;

    @Column(name = LOGIN_NAME)
    private String loginName;

    @Column(name = HASH_PASSWORD)
    private String hashPassword;

    @Column(name = DISPLAY_NAME)
    private String displayName;

    @Column(name = EMAIL_ADDRESS)
    private String emailAddress;

    @Column(name = NEW_EMAIL_ADDRESS)
    private String newEmailAddress;

    @Column(name = EMAIL_ADDRESS_VERIFIED)
    private Boolean emailAddressVerified = Boolean.FALSE;

    @Column(name = VERIFY_CODE)
    private String verifyCode;

    @Column(name = VERIFY_EXPIRED_CODE_TIME)
    private String verifyExpiredCodeTime;

    @Column(name = RESET_PASSWORD_CODE)
    private String resetPasswordCode;

    @Column(name = RESET_PASSWORD_EXPIRED_CODE_TIME)
    private String resetPasswordExpiredCodeTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = ROLES)
    private List<String> roles = new ArrayList<>();

    @OneToMany(
            mappedBy = "userProfile",
            cascade = CascadeType.ALL
    )
    private List<RefreshToken> refreshTokens;

    public UserProfile(String loginName, String hashPassword, String displayName, String emailAddress, List<String> roles) {
        this.loginName = loginName;
        this.hashPassword = hashPassword;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.roles = roles;
    }
}
