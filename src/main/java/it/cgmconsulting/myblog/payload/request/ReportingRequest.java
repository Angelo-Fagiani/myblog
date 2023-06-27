package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ReportingRequest {

    @Min(1)
    private long commentId;

    @NotBlank
    private String reason;

}
