package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class LabelDTO {
    private Long id;

    private String name;

    private LocalDateTime createdAt;
}