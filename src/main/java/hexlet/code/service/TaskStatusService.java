package hexlet.code.service;

import java.util.List;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.dto.TaskStatusCreateDTO;
//import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper TaskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        var tasks = repository.findAll();
        var result = tasks.stream()
                .map(TaskStatusMapper::map)
                .toList();
        return result;
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskData) {
        var task = TaskStatusMapper.map(taskData);
        repository.save(task);
        var taskDTO = TaskStatusMapper.map(task);
        return taskDTO;
    }

    public TaskStatusDTO findById(Long id) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("")); // ResourceNotFoundException("Not Found: " + id));
        var taskDTO = TaskStatusMapper.map(task);
        return taskDTO;
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO taskData) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("")); //ResourceNotFoundException("Not Found"));
        TaskStatusMapper.update(taskData, task);
        repository.save(task);
        var taskDTO = TaskStatusMapper.map(task);
        return taskDTO;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}