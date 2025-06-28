package hexlet.code.mapper;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, LabelMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")//, qualifiedByName = "mapStatusByName")   // LAST FIX IS HERE
    @Mapping(target = "assignee", source = "assignee_id", qualifiedByName = "mapAssignee")// PROBLEM!
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "mapLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "assignee_id", source = "assignee.id")
    @Mapping(target = "status", source = "taskStatus")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "mapLabelsToDTO")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    //@Named("mapStatusByName")
    protected TaskStatus map(String statusSlug) { //!!!
        return taskStatusRepository.findBySlug(statusSlug)
                .orElseThrow(() -> new IllegalArgumentException("No TaskStatus with slug: " + statusSlug));
    }

    protected String map(TaskStatus status) { //!!!
        return status.getName();
    }

    @Named("mapAssignee")
    protected User map(Long id) { //!!!
        if (id == null) return null;

        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user with id " + id));
    }

    @Named("mapLabels")
    public Set<Label> mapLabels(Set<Long> labelIds) {
        if (labelIds == null) {
            return Collections.emptySet();
        }
        return labelIds.stream()
                .map(id -> labelRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Label not found: " + id)))
                .collect(Collectors.toSet());
    }

    @Named("mapLabelsToDTO")
    public Set<Long> mapLabelsToDTO(Set<Label> labels) {
        if (labels == null) {
            return Collections.emptySet();
        }
        return labels.stream()
                .map(label -> label.getId())
                .collect(Collectors.toSet());
    }

}
