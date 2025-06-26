package hexlet.code.dto;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskStatusDTO {
    private Long id;

    private String name;

    private String slug;

    private LocalDateTime createdAt;
}