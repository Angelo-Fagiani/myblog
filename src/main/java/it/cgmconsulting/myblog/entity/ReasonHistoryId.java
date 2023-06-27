package it.cgmconsulting.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReasonHistoryId implements Serializable {

    @ManyToOne
    @JoinColumn(name="reason_id", nullable=false)
    private Reason reason;

    @Column(nullable=false)
    private LocalDate startDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasonHistoryId that = (ReasonHistoryId) o;
        return Objects.equals(reason, that.reason) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, startDate);
    }
}
