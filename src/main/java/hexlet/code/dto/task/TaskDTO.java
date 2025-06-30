package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class TaskDTO {
    private Long id;

    private Integer index;

    private String title;

    private String content;

    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private LocalDate createdAt;

    private Set<Long> taskLabelIds = new HashSet<>();   // обязательно присвоить новый массив

}
