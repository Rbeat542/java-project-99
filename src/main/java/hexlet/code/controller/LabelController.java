package hexlet.code.controller;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    //@PreAuthorize("@userUtils.isAuthor(#id)")
    public ResponseEntity<LabelDTO> patch(@PathVariable Long id, @RequestBody LabelUpdateDTO dto) throws Exception {
        var user = labelService.update(id, dto);

        return ResponseEntity.ok()
                .body(user);
    }


    @DeleteMapping("/labels/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@PreAuthorize("@userUtils.isAuthor(#id)")
    public void delete(@PathVariable Long id) {
        labelService.deleteById(id);
    }
}