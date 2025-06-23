package hexlet.code.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateDTO {

    @NotNull
    private String slug;

    @NotNull
    private String name;

    @NotNull
    private String body;
}