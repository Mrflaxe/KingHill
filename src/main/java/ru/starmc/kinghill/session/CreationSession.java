package ru.starmc.kinghill.session;

import org.bukkit.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CreationSession {

    private final String id;
    
    private Location plateLocation = null;
    private Location pos1 = null;
    private Location pos2 = null;
}
