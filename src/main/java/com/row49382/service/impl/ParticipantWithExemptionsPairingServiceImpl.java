package com.row49382.service.impl;

import com.row49382.domain.Participant;
import com.row49382.service.PairingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ParticipantWithExemptionsPairingServiceImpl implements PairingService {
    private final List<Participant> participants;
    private final Map<String, String[]> exemptionsByParticipantName;
    private final Random random;

    public ParticipantWithExemptionsPairingServiceImpl(
            List<Participant> participants,
            Map<String, String[]> exemptionsByParticipantName,
            Random random) {
        this.participants = participants;
        this.exemptionsByParticipantName = exemptionsByParticipantName;
        this.random = random;
    }

    public void generatePairings() {
        boolean doesNeedReroll;

        do {
            List<Participant> candidateReceivers = new ArrayList<>(this.participants);

            for (int i = 0; i < this.participants.size(); i++) {
                Participant currentParticipant = this.participants.get(i);
                List<Integer> candidateIndexes =
                        this.getCandidateIndexes(candidateReceivers, currentParticipant.getName());

                if (!candidateIndexes.isEmpty()) {
                    int candidateIndex = candidateIndexes.get(this.random.nextInt(candidateIndexes.size()));
                    Participant receiver = candidateReceivers.remove(candidateIndex);
                    receiver.setPicked(true);
                    currentParticipant.setReceiver(receiver);

                }
            }

            if (this.participants.stream()
                    .map(Participant::isPicked)
                    .anyMatch(p -> p == false)) {
                // One or more participant's only recipient options were an exemption
                // we need to reroll again to make sure everyone gets a user without exemption.
                this.participants.forEach(p -> {
                    p.setReceiver(null);
                    p.setPicked(false);
                });
                doesNeedReroll = true;
            }
            else {
                doesNeedReroll = false;
            }
        } while (doesNeedReroll);
    }

    private List<Integer> getCandidateIndexes(
            List<Participant> candidateReceivers,
            String participantName) {
        String[] exemptions = this.exemptionsByParticipantName.get(participantName);
        List<Integer> candidateIndexes = new ArrayList<>();

        for (int i = 0; i < candidateReceivers.size(); i++) {
            final Participant currCandidateReceiver = candidateReceivers.get(i);
            if (!participantName.equals(currCandidateReceiver.getName()) &&
                !currCandidateReceiver.isPicked() &&
                this.isCandidateReceiverNotAnExemption(exemptions, currCandidateReceiver)) {
                candidateIndexes.add(i);
            }
        }

        return candidateIndexes;
    }

    private boolean isCandidateReceiverNotAnExemption(String[] exemptions, Participant candidateReceiver) {
        return exemptions == null || !Arrays.stream(exemptions).anyMatch(e -> e.equals(candidateReceiver.getName()));
    }
}
