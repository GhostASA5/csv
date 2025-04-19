package com.example.project.utils;

import com.example.project.dto.Conflict;
import com.example.project.model.Equipment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mock {

    public static List<Conflict> conflicts = new ArrayList<>();

    public static void addConflict(List<Equipment> equipment) {
        conflicts = new ArrayList<>();
        for (int i = 0; i < equipment.size(); i += 2) {
            conflicts.add(new Conflict(equipment.get(i), equipment.get(i + 1)));
        }
    }
}
