package it.cgmconsulting.myblog.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class PostSearchResponse extends PostBoxResponse{

    // id, immagine, titolo (ereditati da PostBoxResponse)
    // author, data di pubblicazione (con paginazione dei dati)
    private String author; // username di User
    private LocalDateTime updatedAt;

    public PostSearchResponse(long id, String title, String image, String author, LocalDateTime updatedAt) {
        super(id, title, image);
        this.author = author;
        this.updatedAt = updatedAt;
    }


}
