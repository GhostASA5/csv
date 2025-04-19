package com.example.project.repository;

import com.example.project.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Equipment findById(UUID uuid);
}
