package com.etextile.TextileECommerce.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AmazonSesClient {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    public AmazonSimpleEmailService amazonSimpleEmailService;

    public void sendEmail(String senderEmail, String receiverEmail, String emailSubject, String emailContentt) {

        String emailContent = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
                + "    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">\n"
                + "    <title>Hello there,</title>\n"
                + "    <style>\n"
                + "        body { background-color: #f8f9fa; padding: 20px; }\n"
                + "        .email-container { max-width: 600px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }\n"
                + "        .email-header img { max-width: 150px; margin-bottom: 20px; }\n"
                + "        .email-body { font-family: Arial, sans-serif; }\n"
                + "        .email-footer { margin-top: 30px; text-align: center; font-size: 12px; color: #6c757d; }\n"
                + "        .btn-custom { background-color: #28a745; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; }\n"
                + "        .btn-custom:hover { background-color: #218838; }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"email-container\">\n"
                + "        <div class=\"email-header text-center\">\n"
                + "            <img src=\"https://drive.google.com/file/d/1M9oM6L_kDc2xsFLODjJJSVdqllB5umwV/view\" alt=\"Etextile Logo\" />\n"
                + "        </div>\n"
                + "        <div class=\"email-body\">\n"
                + "            <h4>Hello There,</h4>\n"
                + "            <p style=\"font-size: 16px; font-weight: 500;\">" + emailContentt + "</p>\n"
                + "            <p class=\"text-center\">\n"
                + "                <a target=\"_blank\" class=\"btn-custom\" href=\"https://chaitanyadev.shop/user/myorders\">Your Etextile Orders</a>\n"
                + "            </p>\n"
                + "        </div>\n"
                + "        <div class=\"email-footer\">\n"
                + "            <p>&copy; 2024 Etextile. All Rights Reserved.</p>\n"
                + "        </div>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";

        // String senderEmail = "sender@example.com";
        // String receiverEmail = "receiver@example.com";
        // String emailSubject = "Test Email Subject";

        try {
            SendEmailRequest sendEmailRequest = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(receiverEmail))
                    .withMessage(
                            new Message()
                                    .withBody(
                                            new Body()
                                                    .withHtml(
                                                            new Content().withCharset("UTF-8").withData(emailContent)))
                                    .withSubject(new Content().withCharset("UTF-8").withData(emailSubject)))
                    .withSource(senderEmail);
            SendEmailResult result = amazonSimpleEmailService.sendEmail(sendEmailRequest);
            System.out.println(result.getMessageId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
