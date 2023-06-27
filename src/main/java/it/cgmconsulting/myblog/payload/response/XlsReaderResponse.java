package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class XlsReaderResponse {

    private long id;
    private String username;
    private long writtenComments;
    private long reportingsWithBan;
    private boolean enabled;

}
