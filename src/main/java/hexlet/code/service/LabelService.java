package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository repository;

    @Autowired
    private LabelMapper LabelMapper;

    public List<LabelDTO> getAll() {
        var labels = repository.findAll();
        var result = labels.stream()
                .map(LabelMapper::map)
                .toList();
        return result;
    }

    public LabelDTO create(LabelCreateDTO labelData) {
        var label = LabelMapper.map(labelData);
        repository.save(label);
        var labelDTO = LabelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO findById(Long id) throws Exception { //remove throws
        var label = repository.findById(id)
                .orElseThrow(() -> new Exception("")); // ResourceNotFoundException("Not Found: " + id));
        var labelDTO = LabelMapper.map(label);
        return labelDTO;
    }

    public LabelDTO update(Long id, LabelUpdateDTO labelData) throws Exception { //remove throws
        var label = repository.findById(id)
                .orElseThrow(() -> new Exception("")); //ResourceNotFoundException("Not Found"));
        LabelMapper.update(labelData, label);
        repository.save(label);
        var labelDTO = LabelMapper.map(label);
        return labelDTO;
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}