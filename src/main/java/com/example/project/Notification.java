package com.example.project;

import com.example.project.repository.EquipmentRepository;
import com.example.project.repository.PairRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Notification {

    private final PairRepository pairRepository;
    private final EquipmentRepository equipmentRepository;

    @GetMapping("/api/v1/notification")
    public List<Equipment> sendNotification() {
        List<Pair> pairs = pairRepository.findAll();
        List<Equipment> equipments = equipmentRepository.findAll();
        for(Pair pair : pairs) {
            Equipment f = equipmentRepository.findById(pair.getFirst()).orElse(null);
            Equipment s = equipmentRepository.findById(pair.getSecond()).orElse(null);
            equipments.add(f);
            equipments.add(s);
        }
        return equipments;
    }
}
