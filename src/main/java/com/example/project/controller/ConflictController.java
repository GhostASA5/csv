package com.example.project.controller;

import com.example.project.dto.Conflict;
import com.example.project.model.Equipment;
import com.example.project.model.Pair;
import com.example.project.repository.EquipmentRepository;
import com.example.project.repository.PairRepository;
import com.example.project.service.CsvService;
import com.example.project.utils.Mock;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConflictController {

    private final PairRepository pairRepository;
    private final EquipmentRepository equipmentRepository;
    private final CsvService csvService;

    @GetMapping("/conflict")
    public List<Conflict> sendNotification() {
        List<Pair> pairs = pairRepository.findAll();
        List<Conflict> conflicts = new ArrayList<>();
        for(Pair pair : pairs) {
            Equipment f = equipmentRepository.findById(pair.getFirst());
            Equipment s = equipmentRepository.findById(pair.getSecond());
            conflicts.add(new Conflict(f, s));
        }
        return conflicts;
    }

    @PostMapping("/update-conflict")
    public void updateConflict(@RequestBody(required = false) Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    @GetMapping("/dataset")
    public void startAnalyze(@RequestParam("file") MultipartFile file) {
        csvService.updateDataset(file);
    }

    // Для тестов на фронте
    @GetMapping("/conflict/mock")
    public List<Conflict> sendNotificationMock() {
        return Mock.conflicts;
    }
}
