package hexlet.code.dto.taskStatus;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusCreateDTO {

    @NotNull
    @Size(min = 1)
    private String slug;

    @NotNull
    @Size(min = 1)
    private String name;
}
