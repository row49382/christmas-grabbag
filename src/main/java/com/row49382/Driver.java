package com.row49382;

import com.row49382.domain.Participant;
import com.row49382.exception.ApplicationExecutionException;
import com.row49382.service.Emailable;
import com.row49382.service.FileWritable;
import com.row49382.service.PairingGeneratable;
import com.row49382.service.impl.CSVParticipantFileReader;
import com.row49382.service.impl.CSVParticipantExemptionsFileReader;
import com.row49382.service.impl.CSVResultsFileWriter;
import com.row49382.service.impl.ParticipantJavaxMailEmailImpl;
import com.row49382.service.impl.ParticipantWithExemptionsPairingGenerator;
import com.row49382.util.LogbackConfiguration;
import com.row49382.util.PropertiesManager;
import com.row49382.util.impl.ApplicationPropertiesManager;
import com.row49382.util.impl.MailPropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
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

    public static void main(String[] args) throws IOException, ApplicationExecutionException {
        ApplicationPropertiesManager applicationPropertiesManager = new ApplicationPropertiesManager();
        LogbackConfiguration.setLevel(applicationPropertiesManager.getLogLevel());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Application has started with args {}, loading dependencies now.", Arrays.toString(args));
        }

        PropertiesManager mailPropertiesManager = new MailPropertiesManager();

        List<Participant> participants = loadParticipants(applicationPropertiesManager);
        Map<String, String[]> exemptionsByParticipantName = loadExemptions(applicationPropertiesManager);

        PairingGeneratable pairingGenerator = new ParticipantWithExemptionsPairingGenerator(
                participants,
                exemptionsByParticipantName,
                new Random(),
                applicationPropertiesManager);

        Emailable emailingService = new ParticipantJavaxMailEmailImpl(
                applicationPropertiesManager,
                mailPropertiesManager,
                participants);

        FileWritable resultsWriter = new CSVResultsFileWriter(
                applicationPropertiesManager.getResultsFileName(),
                applicationPropertiesManager.getCsvDelimiter(),
                participants);

        LOG.debug("Dependencies loaded successfully. Starting invoker now.");

        Invoker invoker = new Invoker(applicationPropertiesManager, pairingGenerator, emailingService, resultsWriter);
        invoker.invoke();

        LOG.debug("Invoker ran successfully. Shutting down program now.");
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
