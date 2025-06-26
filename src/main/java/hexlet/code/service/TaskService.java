package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskDTO> getAll() {
        var tasks = repository.findAll();
        var result = tasks.stream()
                .map(taskMapper::map)
                .toList();
        return result;
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        if (taskData.getIndex() == null) {
            taskData.setIndex(Math.round(Math.random() * 1000));
        }
        var task = taskMapper.map(taskData);

        var newTask = repository.save(task); // !!!!
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