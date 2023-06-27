package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignupRequest {

	@NotBlank @Size(max=20, min=5)
	private String username;
	@NotBlank @Email
	private String email;
	@NotBlank @Size(min=6)
	@Pattern(regexp = "^[a-zA-Z0-9]{5,15}$",
			message = "Password must be of 5 to 15 length with no special characters")
	private String password;

}
