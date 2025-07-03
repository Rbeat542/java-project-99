package hexlet.code.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@EqualsAndHashCode
public class UserDTO {
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate createdAt;
}
