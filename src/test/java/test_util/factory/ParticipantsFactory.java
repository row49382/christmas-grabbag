package test_util.factory;

import com.row49382.domain.Participant;
import com.row49382.service.impl.CSVParticipantFileReader;

import java.io.IOException;
import java.util.List;

public class ParticipantsFactory {
    private static final String EMPTY_CSV_FILE_NAME = "participants-test-empty.csv";
    private static final String SINGLE_CSV_FILE_NAME = "participants-test-single.csv";
    private static final String MULTIPLE_CSV_FILE_NAME = "participants-test-multiple.csv";
    private static final char DEFAULT_DELIMITER = ',';

    public enum ParticipantTestOption {
        EMPTY,
        SINGLE,
        SINGLE_NULL_RECEIVER,
        MULTIPLE,
        MULTIPLE_NULL_RECEIVERS,
    }

    public static List<Participant> of(ParticipantTestOption participantTestOption) throws IOException {
        List<Participant> participants = null;

        switch (participantTestOption) {
            case EMPTY: {
                participants = new CSVParticipantFileReader(
                        EMPTY_CSV_FILE_NAME,
                        DEFAULT_DELIMITER).read();
                break;
            }
            case SINGLE:
            case SINGLE_NULL_RECEIVER: {
                participants = new CSVParticipantFileReader(
                        SINGLE_CSV_FILE_NAME,
                        DEFAULT_DELIMITER).read();

                break;
            }
            case MULTIPLE:
            case MULTIPLE_NULL_RECEIVERS: {
                participants = new CSVParticipantFileReader(
                        MULTIPLE_CSV_FILE_NAME,
                        DEFAULT_DELIMITER).read();
                break;
            }
        }

        if (participantTestOption == ParticipantTestOption.MULTIPLE ||
            participantTestOption == ParticipantTestOption.SINGLE) {
            setReceivers(participants);
        }

        return participants;
    }

    private static void setReceivers(List<Participant> participants) {
        for (int i = 0; i < participants.size(); i++) {
            int receiverIdentifier = i + 1;
            participants.get(i).setReceiver(
                    new Participant(
                            String.format("receiver%d", receiverIdentifier),
                            String.format("email%d@email.com", receiverIdentifier)));
        }
    }
}

