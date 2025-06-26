package hexlet.code.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;


@Setter
@Getter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements BaseEntity, UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    //@EqualsAndHashCode.Include
    //@OneToMany(mappedBy = "assignee")
    private long id;

    //@ToString.Include
    private String firstName;

    //@ToString.Include
    private String lastName;

    @Column(unique = true)
    @NotNull
    @Email
    //@ToString.Include
    private String email;

    @NotBlank
    private String passwordDigest;

    @Override
    public String getPassword() {
        return passwordDigest;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

  /*  @Override
    public boolean isEnabled() {
        return true;
    }
*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>();
    }

    /*
  @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }*/
}
