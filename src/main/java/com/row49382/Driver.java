package com.row49382;

import com.row49382.domain.Participant;
import com.row49382.service.EmailingService;
import com.row49382.service.PairingService;
import com.row49382.service.impl.CSVParticipantFileReader;
import com.row49382.service.impl.CSVParticipantPairingExemptionsFileReader;
import com.row49382.service.impl.ParticipantEmailingServiceImpl;
import com.row49382.service.impl.ParticipantWithExemptionsPairingServiceImpl;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;
import com.row49382.util.impl.MailPropertiesManager;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * Application to assign all participants a recipient in a grab-bag. Exemptions are applied to
 * keep certain participants from having certain recipients.
 *
 * @author row49382
 * @since 1.0
 */
public class Driver {

    public static void main(String[] args) throws IOException, MessagingException {
        ApplicationPropertiesManager applicationPropertiesManager = new ApplicationPropertiesManager(new Properties());
        PropertiesManager mailPropertiesManager = new MailPropertiesManager(new Properties());

        List<Participant> participants = loadParticipants(applicationPropertiesManager);
        Map<String, String[]> exemptionsByParticipantName = loadExemptions(applicationPropertiesManager);

        PairingService pairingService =
                new ParticipantWithExemptionsPairingServiceImpl(
                        participants,
                        exemptionsByParticipantName,
                        new Random());

        pairingService.generatePairings();

        EmailingService emailingService = new ParticipantEmailingServiceImpl(
                applicationPropertiesManager,
                mailPropertiesManager,
                participants);

        emailingService.sendEmail();
    }

    private static Map<String, String[]> loadExemptions(ApplicationPropertiesManager applicationPropertiesManager)
            throws IOException {
        return new CSVParticipantPairingExemptionsFileReader(
                applicationPropertiesManager.getExemptionsCSVFileName(),
                applicationPropertiesManager.getCsvDelimiter(),
                applicationPropertiesManager.getExemptionsCSVNameExemptionsDelimiter()).read();
    }

    private static List<Participant> loadParticipants(ApplicationPropertiesManager applicationPropertiesManager)
            throws IOException {
        return new CSVParticipantFileReader(
                applicationPropertiesManager.getParticipantsCSVFileName(),
                applicationPropertiesManager.getCsvDelimiter()).read();
    }
}
