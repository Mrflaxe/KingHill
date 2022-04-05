package ru.starmc.kinghill.placeholder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DataType {
    MAXTIME("maxtime"),
    ALLTIME("time");
    
    @Getter
    private final String identifire;
}
