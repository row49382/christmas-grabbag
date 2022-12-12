package com.row49382;

import com.row49382.domain.Participant;
import com.row49382.exception.EmailServiceException;
import com.row49382.exception.PairingGenerateException;
import com.row49382.service.Emailable;
import com.row49382.service.PairingGeneratable;
import com.row49382.service.impl.CSVParticipantFileReader;
import com.row49382.service.impl.CSVParticipantExemptionsFileReader;
import com.row49382.service.impl.ParticipantJavaxMailEmailImpl;
import com.row49382.service.impl.ParticipantWithExemptionsPairingGenerator;
import com.row49382.util.LogbackConfiguration;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;
import com.row49382.util.impl.MailPropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Application to assign all participants a recipient in a grab-bag. Exemptions are applied to
 * keep certain participants from having certain recipients.
 *
 * @author row49382
 * @since 1.0
 */
public class Driver {
    private static final Logger LOG = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) throws IOException {
        ApplicationPropertiesManager applicationPropertiesManager = new ApplicationPropertiesManager();
        PropertiesManager mailPropertiesManager = new MailPropertiesManager();
        LogbackConfiguration.setLevel(applicationPropertiesManager.getLogLevel());

        List<Participant> participants = loadParticipants(applicationPropertiesManager);
        Map<String, String[]> exemptionsByParticipantName = loadExemptions(applicationPropertiesManager);

        PairingGeneratable pairingGenerator = new ParticipantWithExemptionsPairingGenerator(
                participants,
                exemptionsByParticipantName,
                new Random(),
                applicationPropertiesManager);

        try {
            Emailable emailingService = new ParticipantJavaxMailEmailImpl(
                    applicationPropertiesManager,
                    mailPropertiesManager,
                    participants);

            pairingGenerator.generate();
            emailingService.send();
        } catch (PairingGenerateException pge) {
            LOG.error("Error occurred while attempting to generate pairings: {}", pge.getMessage());
        } catch (EmailServiceException ese) {
            LOG.error("Error occurred: {}", ese.getMessage());
        }
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
