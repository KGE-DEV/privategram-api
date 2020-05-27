package com.garrettestrin.PrivateGram.biz;

import com.garrettestrin.PrivateGram.app.PrivateGramConfiguration;
import com.garrettestrin.PrivateGram.data.DataObjects.User;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class BizUtilities {
    private final String user;
    private final String host;
    private final String password;
    private final String siteName;
    private final String siteUrl;
    private final String inviteRequestSubject;
    private final String inviteRequestMessage;
    private final String newUserPasswordResetSubject;
    private final String newUserPasswordResetMessage;
    private final String resetPasswordSubject;
    private final String resetPasswordMessage;

    public BizUtilities(PrivateGramConfiguration config) {
        this.user = config.getEmailUser();
        this.host = config.getEmailHost();
        this.password = config.getEmailPassword();
        this.siteName = config.getSiteName();
        this.siteUrl = config.getSiteDomain();
        this.inviteRequestSubject = config.getInviteRequestSubject();
        this.inviteRequestMessage = config.getInviteRequestMessage();
        this.newUserPasswordResetSubject = config.getNewUserPasswordSetSubject();
        this.newUserPasswordResetMessage = config.getNewUserPasswordSetMessage();
        this.resetPasswordSubject = config.getResetPasswordSubject();
        this.resetPasswordMessage = config.getResetPasswordMessage();
    }

    public boolean sendEmail(String recipient, String subject, String messageBody) {
        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send message
            Transport.send(message);
            log.info("message sent successfully.");
            return true;

        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean sendInviteRequestedEmail(List<User> admins) {
        String subject = String.format(inviteRequestSubject, siteName) ;
        String message = String.format(inviteRequestMessage, siteName, siteUrl);
        for (int i = 0; i < admins.size(); i++) {
            sendEmail(admins.get(i).email, subject, message);
        }
        return true;
    }

    public boolean newUserPasswordSet(String userEmail, String token) {
        String to = userEmail;
        String subject = String.format(newUserPasswordResetSubject, siteName);
        String message = String.format(newUserPasswordResetMessage + token + "&set=true&email=" + userEmail, siteName, siteUrl);
        return sendEmail(to, subject, message);
    }

    public boolean sendResetPasswordEmail(String userEmail, String token) {
        String to = userEmail;
        String subject = String.format(resetPasswordSubject, siteName);
        String message = String.format(resetPasswordMessage + token + "&email=" + userEmail, siteName, siteUrl);
        return sendEmail(to, subject, message);
    }

    public boolean sendPostErrorEmail(List<User> admins, String caption) {
        String subject = String.format("Failed %s Post", siteName) ;
        String message = String.format("Your post on %s failed to save. Please try again. Caption: %s", siteName, caption);
        for (int i = 0; i < admins.size(); i++) {
            sendEmail(admins.get(i).email, subject, message);
        }
        return true;
    }
}
