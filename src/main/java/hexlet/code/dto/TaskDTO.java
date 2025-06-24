package hexlet.code.dto;


import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDTO {
    private Long id;
    private Long authorId;
    private String slug;
    private String name;
    private String body;
    private LocalDateTime createdAt;
}