package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class TaskUpdateDTO {

    @NotNull
    @Size(min = 1)
    private JsonNullable<String> title;

    private JsonNullable<String> content;
}
