package it.cgmconsulting.myblog.payload.response;

import it.cgmconsulting.myblog.entity.ReportingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportingResponse {

    private ReportingStatus status;
    private long commentId;
    private String reporter; // username dell'utente segnalante
    private String commentAuthor; // username dell'autore del commento, ovvero l'utente segnalato
    private String comment; // testo del commento
    private String reason; // motivazione della segnalazione
    private LocalDateTime updatedAt; // data ultima modifica della segnalazione

}
