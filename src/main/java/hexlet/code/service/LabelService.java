package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {

    private final LabelRepository repository;

    private final LabelMapper labelMapper;

    public List<LabelDTO> getAll() {
        var labels = repository.findAll();
        var result = labels.stream()
                .map(labelMapper::map)
                .toList();
        return result;
    }

    public LabelDTO create(LabelCreateDTO labelData) {
        var label = labelMapper.map(labelData);
        repository.save(label);
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO findById(Long id) throws Exception {
        var label = repository.findById(id)
                .orElseThrow(() -> new Exception("Label with id = " + id + " not found"));
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO update(Long id, LabelUpdateDTO labelData) throws Exception {
        var label = repository.findById(id)
                .orElseThrow(() -> new Exception("Label with id = " + id + " not found"));
        labelMapper.update(labelData, label);
        repository.save(label);
        var labelDTO = labelMapper.map(label);
        return labelDTO;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
