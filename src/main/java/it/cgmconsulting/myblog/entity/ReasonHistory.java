package it.cgmconsulting.myblog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor
public class ReasonHistory {

    @EmbeddedId
    private ReasonHistoryId reasonHistoryId;

    private int severity; // corrisponde al numero di giorni di ban; se ban è permanente severity=36500

    private LocalDate endDate;

    public ReasonHistory(ReasonHistoryId reasonHistoryId, int severity) {
        this.reasonHistoryId = reasonHistoryId;
        this.severity = severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasonHistory that = (ReasonHistory) o;
        return Objects.equals(reasonHistoryId, that.reasonHistoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reasonHistoryId);
    }
}
