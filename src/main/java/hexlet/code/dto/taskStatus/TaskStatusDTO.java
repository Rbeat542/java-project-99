package hexlet.code.dto.taskStatus;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusDTO {
    private Long id;

    private String name;

    private String slug;

    private LocalDate createdAt;
}
