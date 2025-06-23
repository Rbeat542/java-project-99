package hexlet.code.controller;

import java.util.List;

import hexlet.code.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.service.TaskService;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
//import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.UserUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<TaskDTO>> index() {
        var tasks = taskService.getAll();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    TaskDTO create(@Valid @RequestBody TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        task.setAuthor(userUtils.getCurrentUser());
        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @GetMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    TaskDTO show(@PathVariable Long id) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception(""));// ResourceNotFoundException("Not Found: " + id));
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @PutMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@userUtils.isAuthor(#id)")
    TaskDTO update(@RequestBody @Valid TaskUpdateDTO taskData, @PathVariable Long id) throws Exception { //remove throws
        var task = repository.findById(id)
                .orElseThrow(() -> new Exception(""));// ResourceNotFoundException("Not Found: " + id));
        taskMapper.update(taskData, task);
        repository.save(task);
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isAuthor(#id)")
    void destroy(@PathVariable Long id) {
        repository.deleteById(id);
    }
}