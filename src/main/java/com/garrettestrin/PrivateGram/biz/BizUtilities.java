package com.garrettestrin.PrivateGram.biz;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BizUtilities {
    private final String user;
    private final String host;
    private final String password;

    public BizUtilities(String user, String host, String emailPassword) {
        this.user = user;
        this.host = host;
        this.password = emailPassword;
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
            System.out.println("message sent successfully....");
            return true;

        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean sendInviteRequestedEmail() {
        // TODO: get admin users from db
        String to = "garrett.estrin@gmail.com";
        String subject = "New Elsiegram Invite Request";
        String message = "A new invite has been requested for ElsieGram.com. Approve users at https://elsiegram.com/admin/invites.";
        return sendEmail(to, subject, message);
    }

    public boolean newUserPasswordReset(String userEmail, String token) {
        String to = userEmail;
        String subject = "Elsiegram Registration";
        String message = "You have been accepted to Elsiegram.com. Please follow the link to set your password: https://elsiegram.com/reset-password?token=" + token;
        return sendEmail(to, subject, message);
    }

    public boolean sendResetPasswordEmail(String userEmail, String token) {
        String to = userEmail;
        String subject = "Elsiegram Password Reset";
        String message = "You have requested a new password for Elisegram.com. Please follow this link to reset your password: https://elsiegram.com/reset-password?token=" + token;
        return sendEmail(to, subject, message);
    }
}
