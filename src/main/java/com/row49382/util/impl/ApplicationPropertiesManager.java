package com.row49382.util.impl;

import com.row49382.util.PropertiesManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ApplicationPropertiesManager extends PropertiesManager {
    private static final String APP_PROPERTIES_FILE_NAME = "application.properties";
    private static final String APP_PROPERTIES_OFFICIANT_EMAIL_KEY = "officiant.email";
    private static final String APP_PROPERTIES_APPLICATION_FROM_EMAIL_ADDRESS_KEY = "application.email.account.from_email";
    private static final String APP_PROPERTIES_APPLICATION_FROM_EMAIL_USERNAME_KEY = "application.email.account.username";
    private static final String APP_PROPERTIES_APPLICATION_FROM_EMAIL_PASSWORD_KEY = "application.email.account.password";
    private static final String APP_PROPERTIES_EXEMPTIONS_CSV_FILE_NAME_KEY = "application.csv.exemptions.file_name";
    private static final String APPLICATION_CSV_EXEMPTIONS_NAME_EXEMPTIONS_DELIMITER = "application.csv.exemptions.name_exemptions_delimiter";
    private static final String APP_PROPERTIES_PARTICIPANTS_CSV_FILE_NAME_KEY = "application.csv.participants.file_name";
    private static final String APP_PROPERTIES_CSV_DELIMITER_KEY = "application.csv.delimiter";
    private static final String APP_PROPERTIES_PAIRING_MAX_RETRY_COUNT_KEY = "application.pairing.maxretrycount";
    private static final String APP_PROPERTIES_LOG_LEVEL_KEY = "application.log.level";
    private static final String APP_PROPERTIES_EMAIL_DO_SEND_KEY = "application.email.do_send";
    private static final String APP_PROPERTIES_RESULTS_DO_SAVE_KEY = "application.results.do_save";
    private static final String APP_PROPERTIES_RESULTS_FILE_NAME_KEY = "application.results.file_name";
    private static final String APP_PROPERTIES_RESULTS_DIRECTORY_KEY = "application.results.results_directory";

    private final String officiantEmail;
    private final String applicationFromEmailAddress;
    private final String applicationFromEmailUserName;
    private final String applicationFromEmailPassword;
    private final String exemptionsCSVFileName;
    private final char exemptionsCSVNameExemptionsDelimiter;
    private final String participantsCSVFileName;
    private final char csvDelimiter;
    private final int pairingMaxRetryCount;
    private final String logLevel;
    private final boolean sendEmail;
    private final boolean saveResults;
    private final String resultsFileName;
    private final String resultsDirectory;

    public ApplicationPropertiesManager() {
        super(APP_PROPERTIES_FILE_NAME);

        this.officiantEmail = (String) properties.get(APP_PROPERTIES_OFFICIANT_EMAIL_KEY);
        this.applicationFromEmailAddress = (String) properties.get(APP_PROPERTIES_APPLICATION_FROM_EMAIL_ADDRESS_KEY);
        this.applicationFromEmailUserName = (String) properties.get(APP_PROPERTIES_APPLICATION_FROM_EMAIL_USERNAME_KEY);
        this.applicationFromEmailPassword = (String) properties.get(APP_PROPERTIES_APPLICATION_FROM_EMAIL_PASSWORD_KEY);
        this.exemptionsCSVFileName = (String) properties.get(APP_PROPERTIES_EXEMPTIONS_CSV_FILE_NAME_KEY);
        this.exemptionsCSVNameExemptionsDelimiter = ((String) properties.get(APPLICATION_CSV_EXEMPTIONS_NAME_EXEMPTIONS_DELIMITER)).charAt(0);
        this.participantsCSVFileName = (String) properties.get(APP_PROPERTIES_PARTICIPANTS_CSV_FILE_NAME_KEY);
        this.csvDelimiter = ((String) properties.get(APP_PROPERTIES_CSV_DELIMITER_KEY)).charAt(0);
        this.pairingMaxRetryCount = Integer.parseInt((String) properties.get(APP_PROPERTIES_PAIRING_MAX_RETRY_COUNT_KEY));
        this.logLevel = (String) properties.get(APP_PROPERTIES_LOG_LEVEL_KEY);
        this.sendEmail = Boolean.parseBoolean((String) properties.get(APP_PROPERTIES_EMAIL_DO_SEND_KEY));
        this.saveResults = Boolean.parseBoolean((String) properties.get(APP_PROPERTIES_RESULTS_DO_SAVE_KEY));
        String resultsFileNameValue = (String) properties.get(APP_PROPERTIES_RESULTS_FILE_NAME_KEY);
        this.resultsDirectory = (String) properties.get(APP_PROPERTIES_RESULTS_DIRECTORY_KEY);

        Objects.requireNonNull(this.officiantEmail);
        Objects.requireNonNull(this.applicationFromEmailAddress);
        Objects.requireNonNull(this.applicationFromEmailUserName);
        Objects.requireNonNull(this.applicationFromEmailPassword);
        Objects.requireNonNull(this.exemptionsCSVFileName);
        Objects.requireNonNull(this.participantsCSVFileName);
        Objects.requireNonNull(this.logLevel);

        if (this.saveResults) {
            Objects.requireNonNull(resultsFileNameValue);
            Objects.requireNonNull(this.resultsDirectory);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM_dd_yyyy_hh_mm_ss");
            resultsFileNameValue = String.format(resultsFileNameValue, formatter.format(LocalDateTime.now()));
        }

        this.resultsFileName = resultsFileNameValue;
    }

    public String getOfficiantEmail() {
        return officiantEmail;
    }

    public String getApplicationFromEmailAddress() {
        return applicationFromEmailAddress;
    }

    public String getApplicationFromEmailUserName() {
        return applicationFromEmailUserName;
    }

    public String getApplicationFromEmailPassword() {
        return applicationFromEmailPassword;
    }

    public String getExemptionsCSVFileName() {
        return exemptionsCSVFileName;
    }

    public char getExemptionsCSVNameExemptionsDelimiter() {
        return exemptionsCSVNameExemptionsDelimiter;
    }

    public String getParticipantsCSVFileName() {
        return participantsCSVFileName;
    }

    public char getCsvDelimiter() {
        return csvDelimiter;
    }

    public int getPairingMaxRetryCount() {
        return pairingMaxRetryCount;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public boolean doSendEmail() {
        return sendEmail;
    }

    public boolean doSaveResults() {
        return this.saveResults;
    }

    public String getResultsFileName() {
        return this.resultsFileName;
    }

    public String getResultsDirectory() {
        return this.resultsDirectory;
    }
}
