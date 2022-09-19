package com.row49382;

import com.row49382.domain.Participant;
import com.row49382.service.EmailingService;
import com.row49382.service.PairingService;
import com.row49382.service.impl.CSVParticipantFileReader;
import com.row49382.service.impl.CSVParticipantPairingExemptionsFileReader;
import com.row49382.service.impl.ParticipantEmailingServiceImpl;
import com.row49382.service.impl.ParticipantWithExemptionsPairingServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Driver {
    public static void main(String[] args) throws IOException {
        List<Participant> participants = loadParticipants();
        Map<String, String[]> exemptionsByParticipantName = loadExemptions();

        PairingService pairingService =
                new ParticipantWithExemptionsPairingServiceImpl(
                        participants,
                        exemptionsByParticipantName);

        pairingService.generatePairings();

        EmailingService emailingService = new ParticipantEmailingServiceImpl(participants);
        emailingService.sendEmail();
    }

    private static Map<String, String[]> loadExemptions() throws IOException {
        // TODO: load from properties file
        String exemptionFileName = "";
        char delimiter = ',';
        char exemptionsDelimiter = ';';

        return new CSVParticipantPairingExemptionsFileReader(
                exemptionFileName,
                delimiter,
                exemptionsDelimiter).read();
    }

    private static List<Participant> loadParticipants() throws IOException {
        // TODO: load from properties file
        String exemptionFileName = "";
        char delimiter = ',';

        return new CSVParticipantFileReader(exemptionFileName, delimiter).read();
    }
}
