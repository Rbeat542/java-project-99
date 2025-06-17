package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter

//@Entity
//@EntityListeners(AuditingEntityListener.class)
public class UserUpdateDTO  {

    @NotBlank
    private JsonNullable<String> firstName;

    @NotBlank
    private JsonNullable<String> lastName;

    @NotBlank
    @Email
    private JsonNullable<String> email;

    @NotBlank
    private JsonNullable<String> password;

/*
    public UserUpdateDTO(JsonNullable<String> email, JsonNullable<String> password) {
        this.email = email;
        this.password = password;
    }
*/
}
