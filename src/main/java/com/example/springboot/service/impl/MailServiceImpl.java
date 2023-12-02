package com.example.springboot.service.impl;

import com.example.springboot.entity.MultipleChoiceTest;
import com.example.springboot.entity.UserProfile;
import com.example.springboot.exception.EmailAddressVerifiedByAnotherUser;
import com.example.springboot.exception.InValidUserStatusException;
import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.repository.ClassroomRegistrationRepository;
import com.example.springboot.repository.UserProfileRepository;
import com.example.springboot.service.MailService;
import com.example.springboot.service.ThymeleafService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
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

    @Autowired
    ClassroomRegistrationRepository classroomRegistrationRepository;

    private Message getEmailMessage() {
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
        return message;
    }

    @Override
    @Async
    public void sendTestUpdatedNotificationEmail(MultipleChoiceTest multipleChoiceTest) {
        List<String> registerUserEmails =
                classroomRegistrationRepository.findUserEmailOfClassroom(multipleChoiceTest.getClassRoom().getId());
        sendEmailTestUpdatedNotification(multipleChoiceTest, registerUserEmails);
    }

    @Async
    protected void sendEmailTestUpdatedNotification(MultipleChoiceTest multipleChoiceTest, List<String> registerUserEmails) {
        Timestamp stamp = new Timestamp(multipleChoiceTest.getStartDate());
        Date date = new Date(stamp.getTime());
        String startDate = String.format("%s:%s %s/%s", date.getHours(), date.getMinutes(), date.getDate(), date.getMonth()+1);
        String classroomName = multipleChoiceTest.getClassRoom().getClassName();
        String testingTime = multipleChoiceTest.getTestingTime().toString() + " minutes";
        String testName = multipleChoiceTest.getTestName();
        try {
            Message message = getEmailMessage();
            String emails = String.join(",", registerUserEmails);
            // start send mail
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emails));
            message.setFrom(new InternetAddress(email));
            message.setSubject("[ONLINE EXAM PLATFORM] Your exam has been updated!");
            message.setContent(
                    thymeleafService.getTestUpdatedNotificationMailContent
                            (classroomName, testName, startDate, testingTime), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendTestCreatedNotificationEmail(Long classroomId, MultipleChoiceTest multipleChoiceTest) {
    List<String> registerUserEmails =
            classroomRegistrationRepository.findUserEmailOfClassroom(classroomId);
        sendEmailTestCreatedNotification(registerUserEmails, multipleChoiceTest);
    }

    @Override
    @Async
    public void sendTestDeletedNotificationEmail(MultipleChoiceTest multipleChoiceTest) {
        List<String> registerUserEmails =
                classroomRegistrationRepository.findUserEmailOfClassroom(multipleChoiceTest.getClassRoom().getId());
        sendEmailTestDeletedNotification(registerUserEmails, multipleChoiceTest);
    }
    @Async
    protected void sendEmailTestDeletedNotification(List<String> registerUserEmails, MultipleChoiceTest multipleChoiceTest) {
        Timestamp stamp = new Timestamp(multipleChoiceTest.getStartDate());
        Date date = new Date(stamp.getTime());
        String startDate = String.format("%s:%s %s/%s", date.getHours(), date.getMinutes(), date.getDate(), date.getMonth()+1);
        String classroomName = multipleChoiceTest.getClassRoom().getClassName();
        String testingTime = multipleChoiceTest.getTestingTime().toString() + " minutes";
        String testName = multipleChoiceTest.getTestName();
        try {
            Message message = getEmailMessage();
            String emails = String.join(",", registerUserEmails);
            // start send mail
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emails));
            message.setFrom(new InternetAddress(email));
            message.setSubject("[ONLINE EXAM PLATFORM] Your exam has been cancelled!");
            message.setContent(
                    thymeleafService.getTestDeletedNotificationMailContent
                            (classroomName, testName, startDate, testingTime), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    protected void sendEmailTestCreatedNotification(List<String> registerUserEmails, MultipleChoiceTest multipleChoiceTest) {
        Timestamp stamp = new Timestamp(multipleChoiceTest.getStartDate());
        Date date = new Date(stamp.getTime());
        String startDate = String.format("%s:%s %s/%s", date.getHours(), date.getMinutes(), date.getDate(), date.getMonth()+1);
        String classroomName = multipleChoiceTest.getClassRoom().getClassName();
        String testingTime = multipleChoiceTest.getTestingTime().toString() + " minutes";
        String testName = multipleChoiceTest.getTestName();
        try {
            Message message = getEmailMessage();
            String emails = String.join(",", registerUserEmails);
            // start send mail
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emails));
            message.setFrom(new InternetAddress(email));
            message.setSubject("[ONLINE EXAM PLATFORM] Your classroom has a new exam!");
            message.setContent(
                    thymeleafService.getTestCreatedNotificationMailContent
                            (classroomName, testName, startDate, testingTime), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send verification code to user for verify email based on login name
     *
     * @param loginName : The login name
     */
    @Override
    @Async
    public ResponseEntity<?> sendVerificationEmail(String loginName) {


        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        String verificationCode = String.valueOf(rnd.nextInt(999999));


        UserProfile userProfile = userProfileRepository.findOneByLoginName(loginName).orElseThrow(
                () -> new UsernameNotFoundException(loginName)
        );
        String checkEmailAddress = userProfile.getEmailAddress();

        // If email address has been verified without value of new_email_address column
        if (Objects.isNull(userProfile.getNewEmailAddress()) && userProfile.getIsEmailAddressVerified()) {
            throw new InValidUserStatusException();
        }

        // If new email address is not null, make sure that this email has not been verified by another user
        if(Objects.nonNull(userProfile.getNewEmailAddress()) && userProfile.getIsEmailAddressVerified()){
            checkEmailAddress = userProfile.getNewEmailAddress();
            // if new email address equals to old verified email address
            // Just for testing, we do not allow users to update the new email address
            // to the same as the old verified email address
            if(userProfile.getEmailAddress().equals(checkEmailAddress)){
                log.error("New email address is the same as value with verified email address : " + checkEmailAddress);
                userProfile.setNewEmailAddress(null);
                return ResponseEntity.noContent().build();
            }
        }
        Optional<UserProfile> value = userProfileRepository.findOneByEmailAddressVerified(checkEmailAddress);
        if (value.isPresent()){
            throw new EmailAddressVerifiedByAnotherUser(checkEmailAddress);
        }
        // update verification code and expired time into database
        updateVerificationCode(verificationCode, userProfile);
        sendEmailVerification(verificationCode, checkEmailAddress);

        return ResponseEntity.noContent().build();
    }

    @Async
    protected void sendEmailVerification(String verificationCode, String checkEmailAddress) {
        try {
            Message message = getEmailMessage();
            // start send mail
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(checkEmailAddress)});
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
     * @param userProfile      : The {@link UserProfile}
     */
    private void updateVerificationCode(String verificationCode, UserProfile userProfile) {
        userProfile.setVerificationExpiredCodeTime(Instant.now().plusSeconds(verificationCodeTime * 60));
        userProfile.setVerificationCode(verificationCode);
        userProfileRepository.save(userProfile);
    }

    @Override
    @Async
    public ResponseEntity<?> sendResetPasswordEmail(String emailAddress) {

        int length = 6;
        boolean useLetters = true;
        boolean useNumbers = false;
        String resetPasswordCode = RandomStringUtils.random(length, useLetters, useNumbers);

        Optional<UserProfile> value = userProfileRepository.findOneByEmailAddressVerified(emailAddress);
        if (value.isEmpty()) {
            throw new UserNotFoundException();
        }
        UserProfile userProfile = value.get();
        // update verification code and expired time into database
        updateResetPasswordCode(resetPasswordCode, userProfile);
        sendEmailResetPassword(resetPasswordCode, userProfile);

        return ResponseEntity.noContent().build();
    }

    @Async
    protected void sendEmailResetPassword(String resetPasswordCode, UserProfile userProfile) {
        try {
            Message message = getEmailMessage();
            // start send mail
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(userProfile.getEmailAddress())});
            message.setFrom(new InternetAddress(email));
            message.setSubject("[ONLINE EXAM PLATFORM] Reset your account password!");
            message.setContent(thymeleafService.getResetPasswordMailContent(resetPasswordCode), CONTENT_TYPE_TEXT_HTML);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void updateResetPasswordCode(String resetPasswordCode, UserProfile userProfile) {
        userProfile.setResetPasswordExpiredCodeTime(Instant.now().plusSeconds(resetPasswordCodeTime * 60));
        userProfile.setResetPasswordCode(resetPasswordCode);
        userProfileRepository.save(userProfile);
    }
}
