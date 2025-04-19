package com.example.project;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Conflict {

    private Equipment first;

    private Equipment second;
}
