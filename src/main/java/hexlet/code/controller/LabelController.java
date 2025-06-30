package hexlet.code.controller;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("/labels")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        var labels = labelService.getAll();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @PostMapping("/labels")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LabelDTO> create(@Valid @RequestBody LabelCreateDTO userData) {
        var label =  labelService.create(userData);

        return ResponseEntity.status(201)
                .body(label);
    }

    @GetMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabelDTO> show(@PathVariable Long id) throws Exception { //remove throws
        var label =  labelService.findById(id);

        return ResponseEntity.ok()
                .body(label);
    }
    @PutMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabelDTO> patch(@PathVariable Long id, @RequestBody LabelUpdateDTO dto) throws Exception {
        var user = labelService.update(id, dto);

        return ResponseEntity.ok()
                .body(user);
    }

    @DeleteMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelService.deleteById(id);
    }
}
