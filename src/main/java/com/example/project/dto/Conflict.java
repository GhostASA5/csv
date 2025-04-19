package com.example.project.dto;

import com.example.project.model.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Conflict {

    private Equipment main;

    private Equipment child;
}
