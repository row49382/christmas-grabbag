package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.PairingGeneratable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ParticipantWithExemptionsPairingGenerator implements PairingGeneratable {
    private final List<Participant> participants;
    private final Map<String, String[]> exemptionsByParticipantName;
    private final Random random;

    public ParticipantWithExemptionsPairingGenerator(
            List<Participant> participants,
            Map<String, String[]> exemptionsByParticipantName,
            Random random) {
        this.participants = participants;
        this.exemptionsByParticipantName = exemptionsByParticipantName;
        this.random = random;
    }

    public void generate() {
        boolean doesNeedReroll;

        do {
            List<Participant> candidateReceivers = new ArrayList<>(this.participants);

            for (Participant currentParticipant : this.participants) {
                List<Integer> candidateReceiverIndexesForCurrentParticipant =
                        this.getCandidateReceiverIndexesForCurrentParticipant(
                                candidateReceivers,
                                currentParticipant.getName());

                if (!candidateReceiverIndexesForCurrentParticipant.isEmpty()) {
                    int candidateIndex = candidateReceiverIndexesForCurrentParticipant.get(
                            this.random.nextInt(candidateReceiverIndexesForCurrentParticipant.size()));
                    
                    Participant receiver = candidateReceivers.remove(candidateIndex);
                    receiver.setPicked(true);
                    currentParticipant.setReceiver(receiver);
                }
            }

            if (this.participants.stream().anyMatch(p -> !p.isPicked())) {
                // One or more participant's only remaining recipient options were an exemption.
                // Re-roll again to make sure everyone has a non-exempt participant as their recipient.
                this.participants.forEach(p -> {
                    p.setReceiver(null);
                    p.setPicked(false);
                });
                doesNeedReroll = true;
            } else {
                doesNeedReroll = false;
            }
        } while (doesNeedReroll);
    }

    private List<Integer> getCandidateReceiverIndexesForCurrentParticipant(
            List<Participant> candidateReceivers,
            String participantName) {
        String[] exemptions = this.exemptionsByParticipantName.get(participantName);
        List<Integer> candidateIndexes = new ArrayList<>();

        for (int i = 0; i < candidateReceivers.size(); i++) {
            final Participant currCandidateReceiver = candidateReceivers.get(i);
            if (this.isCandidateReceiverNotSameAsParticipant(participantName, currCandidateReceiver.getName()) &&
                !currCandidateReceiver.isPicked() &&
                this.isCandidateReceiverNotAnExemption(exemptions, currCandidateReceiver)) {
                candidateIndexes.add(i);
            }
        }

        return candidateIndexes;
    }

    private boolean isCandidateReceiverNotSameAsParticipant(String participantName, String currCandidateReceiverName) {
        return !participantName.equals(currCandidateReceiverName);
    }

    private boolean isCandidateReceiverNotAnExemption(String[] exemptions, Participant candidateReceiver) {
        return exemptions == null || Arrays.stream(exemptions).noneMatch(e -> e.equals(candidateReceiver.getName()));
    }
}
