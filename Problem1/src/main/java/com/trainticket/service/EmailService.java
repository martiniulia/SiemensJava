package com.trainticket.service;

import com.trainticket.config.AppConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {
    public static void sendEmail(String to, String subject, String content) {
        String host = AppConfig.get("mail.smtp.host");
        String port = AppConfig.get("mail.smtp.port");
        String username = AppConfig.get("mail.username");
        String password = AppConfig.get("mail.password");
        String from = AppConfig.get("mail.from");

        if (host == null || username == null || "smtp.example.com".equals(host)) {
            System.out.println("\n[MOCK EMAIL SENT TO: " + to + "]");
            System.out.println("Subject: " + subject);
            System.out.println("Content:\n" + content);
            System.out.println("[END OF MOCK EMAIL]\n");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        props.put("mail.smtp.connectiontimeout", "3000");
        props.put("mail.smtp.timeout", "3000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email successfully sent to " + to);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
