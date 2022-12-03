package com.row49382.util.impl;

import com.row49382.util.PropertiesManager;

import java.util.Objects;
import java.util.Properties;

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

    private final String officiantEmail;
    private final String applicationFromEmailAddress;
    private final String applicationFromEmailUserName;
    private final String applicationFromEmailPassword;
    private final String exemptionsCSVFileName;
    private final char exemptionsCSVNameExemptionsDelimiter;
    private final String participantsCSVFileName;
    private final char csvDelimiter;

    public ApplicationPropertiesManager(Properties properties) {
        super(APP_PROPERTIES_FILE_NAME, properties);

        this.officiantEmail = (String) properties.get(APP_PROPERTIES_OFFICIANT_EMAIL_KEY);
        this.applicationFromEmailAddress = (String) properties.get(APP_PROPERTIES_APPLICATION_FROM_EMAIL_ADDRESS_KEY);
        this.applicationFromEmailUserName = (String) properties.get(APP_PROPERTIES_APPLICATION_FROM_EMAIL_USERNAME_KEY);
        this.applicationFromEmailPassword = (String) properties.get(APP_PROPERTIES_APPLICATION_FROM_EMAIL_PASSWORD_KEY);
        this.exemptionsCSVFileName = (String) properties.get(APP_PROPERTIES_EXEMPTIONS_CSV_FILE_NAME_KEY);
        this.exemptionsCSVNameExemptionsDelimiter = ((String) properties.get(APPLICATION_CSV_EXEMPTIONS_NAME_EXEMPTIONS_DELIMITER)).charAt(0);
        this.participantsCSVFileName = (String) properties.get(APP_PROPERTIES_PARTICIPANTS_CSV_FILE_NAME_KEY);
        this.csvDelimiter = ((String) properties.get(APP_PROPERTIES_CSV_DELIMITER_KEY)).charAt(0);

        Objects.requireNonNull(this.officiantEmail);
        Objects.requireNonNull(this.applicationFromEmailAddress);
        Objects.requireNonNull(this.applicationFromEmailUserName);
        Objects.requireNonNull(this.applicationFromEmailPassword);
        Objects.requireNonNull(this.exemptionsCSVFileName);
        Objects.requireNonNull(this.participantsCSVFileName);
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
}
