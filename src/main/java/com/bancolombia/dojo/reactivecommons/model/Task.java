package com.bancolombia.dojo.reactivecommons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String name;
    private String description;
    private String supervisor;
}
