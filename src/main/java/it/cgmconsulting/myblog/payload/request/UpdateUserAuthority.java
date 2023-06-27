package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
public class UpdateUserAuthority {

    @Min(1)
    private long userId;
    @NotEmpty
    private Set<String> authorities;

}
