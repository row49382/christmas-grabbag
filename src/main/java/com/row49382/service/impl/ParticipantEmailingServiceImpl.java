package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.EmailingService;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;

import javax.mail.Address;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParticipantEmailingServiceImpl implements EmailingService {
    private final List<Participant> participants;
    private final ApplicationPropertiesManager applicationPropertiesManager;
    private final PropertiesManager mailPropertiesManager;

    public ParticipantEmailingServiceImpl(
            ApplicationPropertiesManager applicationPropertiesManager,
            PropertiesManager mailPropertiesManager,
            List<Participant> participants) {
        this.participants = participants;
        this.applicationPropertiesManager = applicationPropertiesManager;
        this.mailPropertiesManager = mailPropertiesManager;
    }

    public void sendEmail() throws MessagingException {
        this.participants.forEach(System.out::println);

        Session session = Session.getInstance(
                this.mailPropertiesManager.getProperties(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                applicationPropertiesManager.getApplicationFromEmailUserName(),
                                applicationPropertiesManager.getApplicationFromEmailPassword());
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.applicationPropertiesManager.getApplicationFromEmailAddress()));
        message.setSubject("Christmas Grabbag Selection");

        InternetAddress[] officiantEmailAddress = InternetAddress.parse(this.applicationPropertiesManager.getOfficiantEmail());

        for (Participant participant : this.participants) {
            message.setSubject(String.format("Christmas Grabbag Selection for %s", participant.getName()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    Stream.concat(
                            Arrays.stream(InternetAddress.parse(participant.getEmail())),
                            Arrays.stream(officiantEmailAddress)).toArray(Address[]::new));

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(participant.toString(), "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        }
    }
}
