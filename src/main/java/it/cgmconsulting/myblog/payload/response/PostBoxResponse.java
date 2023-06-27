package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostBoxResponse {

    // id, immagine, titolo
    private long id;
    private String title;
    private String image;


}
