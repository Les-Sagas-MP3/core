package fr.lessagasmp3.core.pdf.parser;

import fr.lessagasmp3.core.common.constant.SagaStatus;
import org.springframework.stereotype.Service;

@Service
public class StatusParser {

    public SagaStatus parse(String statusString) {
        return switch (statusString.toUpperCase().replace(" ", "")) {
            case "TERMINÉE", "TERMINER" -> SagaStatus.FINISHED;
            case "EN COURS" -> SagaStatus.IN_PROGRESS;
            case "ABANDONNÉE", "ABANDONNÉ" -> SagaStatus.ABANDONED;
            default -> SagaStatus.UNKNOWN;
        };
    }


}
