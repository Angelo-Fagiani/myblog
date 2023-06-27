package it.cgmconsulting.myblog.mail;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Mail {

    private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailContent;
    private String mailMimeType;
}
