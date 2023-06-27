package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UpdateUserProfile {

    @NotBlank
    @Size(max=20, min=5)
    private String newUsername;
    @NotBlank @Email
    private String newEmail;
}
