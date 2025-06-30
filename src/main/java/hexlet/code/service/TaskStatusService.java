package hexlet.code.service;

import java.util.List;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.mapper.TaskStatusMapper;

@Service
public final class TaskStatusService {
    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        var tasks = repository.findAll();
        var result = tasks.stream()
                .map(taskStatusMapper::map)
                .toList();
        return result;
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskData) {
        var task = taskStatusMapper.map(taskData);
        repository.save(task);
        var taskDTO = taskStatusMapper.map(task);
        return taskDTO;
    }

    public TaskStatusDTO findById(Long id) throws Exception {
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("Not Found: " + id));
        var taskDTO = taskStatusMapper.map(task);
        return taskDTO;
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO taskData) throws Exception {
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception("Not Found: " + id));
        taskStatusMapper.update(taskData, task);
        repository.save(task);
        var taskDTO = taskStatusMapper.map(task);
        return taskDTO;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
