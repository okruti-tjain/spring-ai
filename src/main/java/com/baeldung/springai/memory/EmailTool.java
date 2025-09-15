package com.baeldung.springai.memory;

import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Component
public class EmailTool {

    private final JavaMailSender mailSender;

    // Hardcoded recipients
    private static final String[] RECIPIENTS = {
            "tanusatishjain9703@gmail.com",
            "tjain@okruti.com",
            "21egjcs156@gitjaipur.com"
    };

    public EmailTool(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Tool(
            name = "sendEmail",
            description = "Send an email to predefined recipients. Input: subject and message body."
    )
    public String sendEmail(String to,String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("support@peoplenest.in");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            return " Email sent to: " + String.join(", ", RECIPIENTS);
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}
