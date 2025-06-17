package hexlet.code.dto;

import hexlet.code.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Setter
@Getter

//@Entity
//@Table(name = "users")
//@EntityListeners(AuditingEntityListener.class)
public class UserCreateDTO {

    @NotBlank
    private String firstName;

    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 3)
    private String password;

    /*public UserCreateDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }*/
}
