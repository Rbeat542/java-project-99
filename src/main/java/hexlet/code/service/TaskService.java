package hexlet.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
//import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.UserUtils;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserUtils userUtils;

    public List<TaskDTO> getAll() {
        var tasks = repository.findAll();
        var result = tasks.stream()
                .map(taskMapper::map)
                .toList();
        return result;
    }

    TaskDTO create(TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        task.setAuthor(userUtils.getCurrentUser());
        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    TaskDTO findById(Long id) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("")); // ResourceNotFoundException("Not Found: " + id));
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    TaskDTO update(TaskUpdateDTO taskData, Long id) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("")); //ResourceNotFoundException("Not Found"));
        taskMapper.update(taskData, task);
        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    void delete(Long id) {
        repository.deleteById(id);
    }
}