package com.row49382;

import com.row49382.domain.Participant;
import com.row49382.exception.EmailServiceException;
import com.row49382.service.Emailable;
import com.row49382.service.PairingGeneratable;
import com.row49382.service.impl.CSVParticipantFileReader;
import com.row49382.service.impl.CSVParticipantExemptionsFileReader;
import com.row49382.service.impl.ParticipantEmailingServiceImpl;
import com.row49382.service.impl.ParticipantWithExemptionsPairingGenerator;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;
import com.row49382.util.impl.MailPropertiesManager;

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

    public static void main(String[] args) throws IOException, EmailServiceException {
        ApplicationPropertiesManager applicationPropertiesManager = new ApplicationPropertiesManager(new Properties());
        PropertiesManager mailPropertiesManager = new MailPropertiesManager(new Properties());

        List<Participant> participants = loadParticipants(applicationPropertiesManager);
        Map<String, String[]> exemptionsByParticipantName = loadExemptions(applicationPropertiesManager);

        PairingGeneratable pairingGenerator =
                new ParticipantWithExemptionsPairingGenerator(
                        participants,
                        exemptionsByParticipantName,
                        new Random());

        pairingGenerator.generate();

        Emailable emailingService = new ParticipantEmailingServiceImpl(
                applicationPropertiesManager,
                mailPropertiesManager,
                participants);

        emailingService.send();
    }

    private static Map<String, String[]> loadExemptions(ApplicationPropertiesManager applicationPropertiesManager)
            throws IOException {
        return new CSVParticipantExemptionsFileReader(
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
