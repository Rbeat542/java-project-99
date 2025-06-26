package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

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
    private LocalDateTime createdAt;

    //@LastModifiedDate //для автоматической генерации


}
