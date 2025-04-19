package com.example.project;

import com.example.project.repository.EquipmentRepository;
import com.example.project.repository.PairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Notification {

    private final PairRepository pairRepository;
    private final EquipmentRepository equipmentRepository;

    @GetMapping("/api/v1/notification")
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
}
