package hexlet.code.dto;

import hexlet.code.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter

//@Entity
//@Table(name = "users")
//@EntityListeners(AuditingEntityListener.class)
public class UserDTO {

  //  @Id
  //  @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String firstName;

    private String lastName;

  //  @NotNull
  //  @Email
    private String email;

  //  @NotNull
  //  @Size(min = 3)
    //private String password;

    //@CreatedDate //для автоматической генерации
    private LocalDate createdAt;

    //@LastModifiedDate //для автоматической генерации
    //private LocalDate updatedAt;

    /*public UserDTO(String firstName, String lastName, String email, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        }*/
}
