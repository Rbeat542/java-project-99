package hexlet.code.controller;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index() {
        var tasks = taskService.getAll();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskCreateDTO userData) {
        var user =  taskService.create(userData);

        return ResponseEntity.status(201)
                .body(user);



    }

    @GetMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskDTO> show(@PathVariable Long id) throws Exception { //remove throws
        var task =  taskService.findById(id);

        return ResponseEntity.ok()
                .body(task);
    }
    @PutMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("@userUtils.isAuthor(#id)")
    public ResponseEntity<TaskDTO> patch(@PathVariable Long id, @RequestBody TaskUpdateDTO dto) throws Exception {
        var user = taskService.update(id, dto);

        return ResponseEntity.ok()
                .body(user);
    }


    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@PreAuthorize("@userUtils.isAuthor(#id)")
    public void delete(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}