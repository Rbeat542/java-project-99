package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hexlet.code.specification.TaskSpecification;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public final class TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskSpecification specification;

    public List<TaskDTO> getAllWithParams(TaskParamsDTO params) {
        if (params.titleCont() == null
                && params.assigneeId() == null
                && params.status() == null
                && params.labelId() == null) {
            return repository.findAll().stream().map(taskMapper::map).toList();
        }

        var spec = specification.build(params);
        var tasks = repository.findAll(spec);
        var result = tasks.stream().map(taskMapper::map);

        return result.collect(Collectors.toList());

        //return repository.findAll(spec).stream().map(taskMapper::map)
        //        .toList();
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        if (taskData.getIndex() == null) {
            taskData.setIndex((int) Math.round(Math.random() * 1000));
        }
        var task = taskMapper.map(taskData);

        var newTask = repository.save(task);
        var taskDTO = taskMapper.map(newTask);
        return taskDTO;
    }

    public TaskDTO findById(Long id) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("")); // ResourceNotFoundException("Not Found: " + id));
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    public TaskDTO update(Long id, TaskUpdateDTO taskData) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("")); //ResourceNotFoundException("Not Found"));
        taskMapper.update(taskData, task);
        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
