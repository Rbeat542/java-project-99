package hexlet.code.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class TaskCreateDTO {

    private Long index;

    @NotNull
    @Size(min = 1)
    private String title;

    private String content;

    @NotNull
    private String status;

    private Long assignee_id;

    @JsonProperty("taskLabelIds")
    private Set<Long> labelIds; // to learn


}