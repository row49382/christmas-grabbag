package com.row49382;

import com.row49382.exception.ApplicationExecutionException;
import com.row49382.exception.EmailServiceException;
import com.row49382.exception.PairingGenerateException;
import com.row49382.service.Emailable;
import com.row49382.service.PairingGeneratable;
import com.row49382.util.impl.ApplicationPropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to invoke the business logic for assigning participants and distributing the
 * results in an email.
 */
public class Invoker {
    private static final Logger LOG = LoggerFactory.getLogger(Invoker.class);

    private final ApplicationPropertiesManager applicationPropertiesManager;
    private final PairingGeneratable pairingGenerator;
    private final Emailable emailingService;

    public Invoker(ApplicationPropertiesManager applicationPropertiesManager,
                   PairingGeneratable pairingGenerator,
                   Emailable emailingService) {
        this.applicationPropertiesManager = applicationPropertiesManager;
        this.pairingGenerator = pairingGenerator;
        this.emailingService = emailingService;
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

            if (this.getApplicationPropertiesManager().isSendEmail()) {
                this.getEmailingService().send();
            } else {
                LOG.debug("Flag to send emails was disabled in application.properties. No emails were sent.");
            }
        } catch (PairingGenerateException pge) {
            LOG.error("Error occurred while attempting to generate pairings: {}", pge.getMessage());
            throw new ApplicationExecutionException();
        } catch (EmailServiceException ese) {
            LOG.error("Error occurred: {}", ese.getMessage());
            throw new ApplicationExecutionException();
        }
    }
}
