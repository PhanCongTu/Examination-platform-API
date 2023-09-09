package com.example.springboot.service.impl;

import com.example.springboot.entity.UserProfile;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.MailService;
import com.example.springboot.service.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Properties;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Value("${config.mail.host}")
    private String host;
    @Value("${config.mail.port}")
    private String port;
    @Value("${config.mail.username}")
    private String email;
    @Value("${config.mail.password}")
    private String password;
    @Value("${send-by-mail.verification-code-time}")
    private Long verificationCodeTime;
    @Value("${send-by-mail.reset-password-code-time}")
    private Long resetPasswordCodeTime;

    @Autowired
    ThymeleafService thymeleafService;

    @Autowired
    UserProfileRepository userProfileRepository;

    /**
     * Send verification code to user for verify email based on login name
     *
     * @param loginName : The login name
     */
    @Override
    @Async
    public void sendVerificationEmail(String loginName) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        Message message = new MimeMessage(session);

        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        String verificationCode = String.valueOf(rnd.nextInt(999999));

        try {
            UserProfile userProfile = userProfileRepository.findOneByLoginName(loginName).orElseThrow(
                    () -> new UsernameNotFoundException(loginName)
            );

            // update verification code and expired time into database
            updateVerificationCode(verificationCode, userProfile);

            // start send mail
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(userProfile.getEmailAddress())});
            message.setFrom(new InternetAddress(email));
            message.setSubject("[ONLINE EXAM PLATFORM] Verify your email");
            message.setContent(thymeleafService.getVerificationMailContent(verificationCode), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update usage information for email verification
     *
     * @param verificationCode : The verification code
     * @param userProfile : The {@link UserProfile}
     */
    private void updateVerificationCode(String verificationCode, UserProfile userProfile) {
        userProfile.setVerificationExpiredCodeTime(Instant.now().plusSeconds(verificationCodeTime * 60) );
        userProfile.setVerificationCode(verificationCode);
        userProfileRepository.save(userProfile);
    }
}
