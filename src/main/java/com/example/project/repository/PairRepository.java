package com.example.project.repository;

import com.example.project.model.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairRepository extends JpaRepository<Pair, Long> {
}
