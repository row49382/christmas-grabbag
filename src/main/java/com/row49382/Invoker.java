package com.row49382;

import com.row49382.exception.ApplicationExecutionException;
import com.row49382.exception.EmailServiceException;
import com.row49382.exception.PairingGenerateException;
import com.row49382.service.Emailable;
import com.row49382.service.FileWritable;
import com.row49382.service.PairingGeneratable;
import com.row49382.util.impl.ApplicationPropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Class to invoke the business logic for assigning participants and distributing the
 * results in an email.
 */
public class Invoker {
    private static final Logger LOG = LoggerFactory.getLogger(Invoker.class);

    private final ApplicationPropertiesManager applicationPropertiesManager;
    private final PairingGeneratable pairingGenerator;
    private final Emailable emailingService;
    private final FileWritable resultsWriter;

    public Invoker(ApplicationPropertiesManager applicationPropertiesManager,
                   PairingGeneratable pairingGenerator,
                   Emailable emailingService,
                   FileWritable resultsWriter) {
        this.applicationPropertiesManager = applicationPropertiesManager;
        this.pairingGenerator = pairingGenerator;
        this.emailingService = emailingService;
        this.resultsWriter = resultsWriter;
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

    public void invoke() throws ApplicationExecutionException {
        try {
            this.getPairingGenerator().generate();

            if (this.getApplicationPropertiesManager().doSendEmail()) {
                this.getEmailingService().send();
            } else {
                LOG.debug("Flag to send emails was disabled in application.properties. No emails were sent.");
            }

            if (this.getApplicationPropertiesManager().doSaveResults()) {
                this.resultsWriter.write();
            }
        } catch (PairingGenerateException pge) {
            LOG.error("Error occurred while attempting to generate pairings: {}", pge.getMessage());
            throw new ApplicationExecutionException();
        } catch (EmailServiceException ese) {
            LOG.error("Error occurred: {}", ese.getMessage());
            throw new ApplicationExecutionException();
        } catch (IOException ioe) {
            LOG.error("Error encountered while saving results: {}", ioe.getMessage());
            throw new ApplicationExecutionException();
        }
    }
}
