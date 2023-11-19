package com.example.springboot.service;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Service
public class ThymeleafService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";

    private static final String VERIFY_MAIL_TEMPLATE_NAME = "verify-mail-template";
    private static final String RESET_PASSWORD_TEMPLATE_NAME = "reset-password-template";
    private static final String TEST_CREATED_NOTIFICATION = "test-created-notification";
    private static final String TEST_DELETED_NOTIFICATION = "test-deleted-notification";
    private static final String TEST_UPDATED_NOTIFICATION = "test-updated-notification";

    private static TemplateEngine templateEngine;

    static {
        templateEngine = emailTemplateEngine();
    }

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public String getVerificationMailContent(String code) {
        final Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(VERIFY_MAIL_TEMPLATE_NAME, context);
    }

    public Object getResetPasswordMailContent(String resetPasswordCode) {
        final Context context = new Context();
        context.setVariable("code", resetPasswordCode);
        return templateEngine.process(RESET_PASSWORD_TEMPLATE_NAME, context);
    }
    public Object getTestCreatedNotificationMailContent(String classroomName, String testName, String startDate, String testingTime) {
        final Context context = new Context();
        context.setVariable("classroomName", classroomName);
        context.setVariable("testName", testName);
        context.setVariable("startDate", startDate);
        context.setVariable("testingTime", testingTime);
        return templateEngine.process(TEST_CREATED_NOTIFICATION, context);
    }

    public Object getTestDeletedNotificationMailContent(String classroomName, String testName, String startDate, String testingTime) {
        final Context context = new Context();
        context.setVariable("classroomName", classroomName);
        context.setVariable("testName", testName);
        context.setVariable("startDate", startDate);
        context.setVariable("testingTime", testingTime);
        return templateEngine.process(TEST_DELETED_NOTIFICATION, context);
    }

    public Object getTestUpdatedNotificationMailContent(String classroomName, String testName, String startDate, String testingTime) {
        final Context context = new Context();
        context.setVariable("classroomName", classroomName);
        context.setVariable("testName", testName);
        context.setVariable("startDate", startDate);
        context.setVariable("testingTime", testingTime);
        return templateEngine.process(TEST_UPDATED_NOTIFICATION, context);
    }
}
