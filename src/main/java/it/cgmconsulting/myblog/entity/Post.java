package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.payload.response.PostSearchResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor
@NamedNativeQuery(
        name="Post.getPostSearchResponseNNQ",
        query="SELECT p.id, p.title, p.image, u.username, p.updated_at " +
                "FROM post p, user u " +
                "WHERE p.published=true " +
                "AND p.author = u.id " +
                "AND p.title LIKE :keyword OR p.content LIKE :keyword " +
                "ORDER BY p.updated_at",
        resultSetMapping="miaQuery"
)
@SqlResultSetMapping(
        name="miaQuery",
        classes = @ConstructorResult(
                targetClass= PostSearchResponse.class,
                columns = {
                     @ColumnResult(name="id", type=Long.class),
                     @ColumnResult(name="title", type=String.class),
                     @ColumnResult(name="image", type=String.class),
                     @ColumnResult(name="username", type=String.class),
                     @ColumnResult(name="updated_at", type= LocalDateTime.class)
                }
        )
)
public class Post extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable=false, unique=true, length=100)
    private String title;

    @Column(nullable=false)
    private String overview;

    @Column(nullable=false, columnDefinition = "TEXT")
    private String content;

    private String image;

    private boolean published=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author", nullable=false)
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="post_categories",
            joinColumns = {@JoinColumn(name="post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="category_name", referencedColumnName = "categoryName")})
    Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    public Post(String title, String overview, String content, User author) {
        this.title = title;
        this.overview = overview;
        this.content = content;
        this.author = author;
    }

    public Post(long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
