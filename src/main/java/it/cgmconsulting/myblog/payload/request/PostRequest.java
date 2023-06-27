package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class PostRequest {

    @NotBlank  @Size(max=100, min=3)
    private String title;
    @NotBlank @Size(max=255, min=15)
    private String overview;
    @NotBlank @Size(min=100, max=65535)
    private String content;

}
