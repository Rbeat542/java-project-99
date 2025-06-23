package hexlet.code.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import hexlet.code.model.Task;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.dto.TaskCreateDTO;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class TaskMapper {
    public abstract Task map(TaskCreateDTO dto);

 //   @Mapping(source = "author.id", target = "authorId")
    public abstract TaskDTO map(Task model);

 //   @Mapping(source = "authorId", target = "author.id")
    public abstract Task map(TaskDTO model);

    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
}