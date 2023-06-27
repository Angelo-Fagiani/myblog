package it.cgmconsulting.myblog.payload.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CommentRequest {

    @NotBlank  @Size(min=1, max=255)
    private String comment;
    @Min(1)
    private long postId;

}
