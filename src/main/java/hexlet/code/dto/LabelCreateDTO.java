package hexlet.code.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabelCreateDTO {

    @NotNull
    private String name;

}