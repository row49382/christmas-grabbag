package com.row49382;

import com.row49382.domain.Participant;
import com.row49382.exception.DriverExecutionException;
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

    private final ApplicationPropertiesManager applicationPropertiesManager;
    private final PropertiesManager mailPropertiesManager;
    private final PairingGeneratable pairingGenerator;
    private final Emailable emailingService;
    private final List<Participant> participants;

    private Driver() throws IOException {
        this.applicationPropertiesManager = new ApplicationPropertiesManager();
        this.mailPropertiesManager = new MailPropertiesManager();

        this.participants = loadParticipants(this.applicationPropertiesManager);
        Map<String, String[]> exemptionsByParticipantName = loadExemptions(this.applicationPropertiesManager);

       this.pairingGenerator = new ParticipantWithExemptionsPairingGenerator(
                participants,
                exemptionsByParticipantName,
                new Random(),
                applicationPropertiesManager);

       this.emailingService = new ParticipantJavaxMailEmailImpl(
                this.applicationPropertiesManager,
                this.mailPropertiesManager,
                this.participants);
    }

    public static Driver getDriver() throws IOException {
        return new Driver();
    }

    public static void main(String[] args) throws IOException, DriverExecutionException {
        Driver app = getDriver();
        LogbackConfiguration.setLevel(app.getApplicationPropertiesManager().getLogLevel());

        try {
            app.getPairingGenerator().generate();

            if (app.getApplicationPropertiesManager().isSendEmail()) {
                app.getEmailingService().send();
            } else {
                LOG.debug("Flag to send emails was disabled in applications.properties. No emails were sent.");
            }
        } catch (PairingGenerateException pge) {
            LOG.error("Error occurred while attempting to generate pairings: {}", pge.getMessage());
            throw new DriverExecutionException();
        } catch (EmailServiceException ese) {
            LOG.error("Error occurred: {}", ese.getMessage());
            throw new DriverExecutionException();
        }
    }

    public ApplicationPropertiesManager getApplicationPropertiesManager() {
        return applicationPropertiesManager;
    }

    public PairingGeneratable getPairingGenerator() {
        return pairingGenerator;
    }

    public Emailable getEmailingService() {
        return emailingService;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    private Map<String, String[]> loadExemptions(ApplicationPropertiesManager applicationPropertiesManager)
            throws IOException {
        return new CSVParticipantExemptionsFileReader(
                applicationPropertiesManager.getExemptionsCSVFileName(),
                applicationPropertiesManager.getCsvDelimiter(),
                applicationPropertiesManager.getExemptionsCSVNameExemptionsDelimiter()).read();
    }

    private List<Participant> loadParticipants(ApplicationPropertiesManager applicationPropertiesManager)
            throws IOException {
        return new CSVParticipantFileReader(
                applicationPropertiesManager.getParticipantsCSVFileName(),
                applicationPropertiesManager.getCsvDelimiter()).read();
    }
}
