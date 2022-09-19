package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.PairingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ParticipantWithExemptionsPairingServiceImpl implements PairingService {
    private final List<Participant> participants;
    private final Map<String, String[]> exemptionsByParticipantName;
    private final Random random;

    public ParticipantWithExemptionsPairingServiceImpl(
            List<Participant> participants,
            Map<String, String[]> exemptionsByParticipantName) {
        this.participants = participants;
        this.exemptionsByParticipantName = exemptionsByParticipantName;
        this.random = new Random();
    }

    public void generatePairings() {
        boolean doesNeedReroll;
        do {
            for (int i = 0; i < this.participants.size(); i++) {
                Participant currentParticipant = this.participants.get(i);
                List<Integer> candidateIndexes =
                        this.getCandidateIndexes(currentParticipant.getName(), i);

                if (!candidateIndexes.isEmpty()) {
                    int candidateIndex = candidateIndexes.get(this.random.nextInt(candidateIndexes.size()));
                    currentParticipant.setReceiver(this.participants.get(candidateIndex));
                }
            }

            if (this.participants.stream()
                    .map(Participant::getReceiver)
                    .anyMatch(Objects::isNull)) {
                doesNeedReroll = true;
            }
            else {
                doesNeedReroll = false;
            }
        } while (doesNeedReroll);
    }

    private List<Integer> getCandidateIndexes(String participantName, int participantIndex) {
        String[] exemptions = this.exemptionsByParticipantName.get(participantName);
        List<Integer> candidateIndexes = new ArrayList<>();
        candidateIndexes.add(participantIndex);

        for (int i = 0; i < this.participants.size(); i++) {
            final Participant currParticipant = this.participants.get(i);
            if (!currParticipant.isPicked() &&
                !Arrays.stream(exemptions).anyMatch(e -> e.equals(currParticipant.getName()))) {
                candidateIndexes.add(i);
            }
        }

        return candidateIndexes;
    }
}
