package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.EmailingService;
import com.row49382.util.ApplicationPropertiesManager;

import java.util.List;

public class ParticipantEmailingServiceImpl implements EmailingService {
    private final List<Participant> participants;
    private final String officiantName;
    private final String officiantEmail;
    private final String applicationFromEmailAddress;
    private final String applicationFromEmailUserName;
    private final String applicationFromEmailPassword;

    public ParticipantEmailingServiceImpl(
            ApplicationPropertiesManager applicationPropertiesManager,
            List<Participant> participants) {
        this.participants = participants;
        this.officiantName = applicationPropertiesManager.getOfficiantName();
        this.officiantEmail = applicationPropertiesManager.getOfficiantEmail();
        this.applicationFromEmailAddress = applicationPropertiesManager.getApplicationFromEmailAddress();
        this.applicationFromEmailUserName = applicationPropertiesManager.getApplicationFromEmailUserName();
        this.applicationFromEmailPassword = applicationPropertiesManager.getApplicationFromEmailPassword();
    }

    public void sendEmail() {
        this.participants.forEach(System.out::println);
    }
}
