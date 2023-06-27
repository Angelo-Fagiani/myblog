package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class ReasonRequest {

    @NotBlank @Length(min=3, max = 30)
    private String reason;

    @Min(1) @Max(36500) // ovvero 100 anni è il ban massimo, il che equivale ad un permaban
    private int severity;

    @NotNull
    private LocalDate startDate;
}
