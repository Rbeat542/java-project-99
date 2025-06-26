package hexlet.code.dto;


import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class TaskDTO {
    private Long id;

    private Long index;

    private String title;

    private String content;

    private String status;

    private Long assignee_id;

    private LocalDateTime createdAt;

    //private Set<LabelDTO> labels; // !!!
    private Set<Long> labelIds;

}