package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.EmailingService;

import java.util.List;

public class ParticipantEmailingServiceImpl implements EmailingService {
    private final List<Participant> participants;

    public ParticipantEmailingServiceImpl(List<Participant> participants) {
        this.participants = participants;
    }

    public void sendEmail() {

    }
}
