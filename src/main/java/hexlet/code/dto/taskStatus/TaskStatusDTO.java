package hexlet.code.dto.taskStatus;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class TaskStatusDTO {
    private Long id;

    private String name;

    private String slug;

    private LocalDate createdAt;
}
