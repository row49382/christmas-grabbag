package com.row49382.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LogbackConfiguration {
    private LogbackConfiguration() { }

    public static void setLevel(String level) {
        if (level == null) {
            return;
        }

        Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(level));
    }
}
