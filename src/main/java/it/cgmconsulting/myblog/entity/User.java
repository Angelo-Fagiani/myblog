package it.cgmconsulting.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class User extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length=20, nullable=false, unique=true)
    private String username;

    @JsonIgnore
    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true)
    private String email;

    private boolean enabled=false;

    private String confirmCode;

    @OneToOne
    @JoinColumn(name="avatar")
    private Avatar avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_authorities",
        joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name="authority_id", referencedColumnName = "id")})
    Set<Authority> authorities = new HashSet<>();

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(long id){
        this.id = id;
    }

    public User(long id, String username, String email, Set<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    public User(String username, String email, String password, Set<Authority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String username, String email, String password, Set<Authority> authorities, String confirmCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.confirmCode = confirmCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
