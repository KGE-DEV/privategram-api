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
        String to = recipient;


        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
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
}
