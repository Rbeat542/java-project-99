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


    /*public UserDTO(String firstName, String lastName, String email, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        }*/
}
