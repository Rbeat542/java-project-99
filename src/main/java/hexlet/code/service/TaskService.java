package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamsDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import hexlet.code.specification.TaskSpecification;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

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

        var spec = specification.build(params); //here is a problem
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