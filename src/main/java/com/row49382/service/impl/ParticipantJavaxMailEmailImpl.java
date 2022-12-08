package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.exception.EmailServiceException;
import com.row49382.service.Emailable;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;

public class ParticipantJavaxMailEmailImpl implements Emailable {
    /**
     * Message template to be sent to all participants.
     *
     * Args:
     *   - 1st %s: The participant's name
     *   - 2nd %s: The participant's receiver's name
     */
    private static final String GRABBAG_MESSAGE_TEMPLATE =
            "Hello %s, <br /><br />" +
            "Thank for participating in the grab-bag! <br /><br />" +
            "You have %s as your recipient. <br /><br />" +
            "Happy gifting!";

    private static final String GRABBAG_SUBJECT_MESSAGE_TEMPLATE = "Grab-bag Selection for %s";
    private static final String MIME_TYPE_TEXT_HTML_CHARSET_UTF8 = "text/html; charset=utf-8";
    private static final String GRABBAG_EMAIL_ERROR_MESSAGE_TEMPLATE = "Error occurred while sending message to %s's email: %s";

    private final List<Participant> participants;
    private final ApplicationPropertiesManager applicationPropertiesManager;
    private final PropertiesManager mailPropertiesManager;

    public ParticipantJavaxMailEmailImpl(
            ApplicationPropertiesManager applicationPropertiesManager,
            PropertiesManager mailPropertiesManager,
            List<Participant> participants) {
        this.participants = participants;
        this.applicationPropertiesManager = applicationPropertiesManager;
        this.mailPropertiesManager = mailPropertiesManager;
    }

    public void send() throws EmailServiceException {
        Session session = this.getSession();

        for (Participant participant : this.participants) {
            Message message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress(this.applicationPropertiesManager.getApplicationFromEmailAddress()));
                message.setContent(this.createMultipart(message, participant));
                Transport.send(message);
            } catch (MessagingException me) {
                throw new EmailServiceException(
                        String.format(
                                GRABBAG_EMAIL_ERROR_MESSAGE_TEMPLATE,
                                participant.getName(),
                                participant.getEmail()),
                        me);
            }
        }
    }

    private Session getSession() {
        return Session.getInstance(
                this.mailPropertiesManager.getProperties(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                applicationPropertiesManager.getApplicationFromEmailUserName(),
                                applicationPropertiesManager.getApplicationFromEmailPassword());
                    }
                });
    }

    private Multipart createMultipart(
            Message message,
            Participant participant) throws MessagingException {
        message.setSubject(String.format(GRABBAG_SUBJECT_MESSAGE_TEMPLATE, participant.getName()));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(
                        String.join(",", participant.getEmail(), this.applicationPropertiesManager.getOfficiantEmail())));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(
                String.format(GRABBAG_MESSAGE_TEMPLATE, participant.getName(), participant.getReceiver().getName()),
                MIME_TYPE_TEXT_HTML_CHARSET_UTF8);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        return multipart;
    }
}
