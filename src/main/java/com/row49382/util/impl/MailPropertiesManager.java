package com.row49382.util.impl;

import com.row49382.util.PropertiesManager;

public class MailPropertiesManager extends PropertiesManager {
    private static final String MAIL_CONFIG_FILE_NAME = "mail-config.properties";

    public MailPropertiesManager() {
        super(MAIL_CONFIG_FILE_NAME);
    }
}
