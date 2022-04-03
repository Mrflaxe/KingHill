package ru.starmc.kinghill.database.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "kinghill_hills")
public class HillModel {
    
    @DatabaseField(columnName = "id", id = true)
    private String name;
    @DatabaseField(columnName = "plate_x")
    private int plateX;
    @DatabaseField(columnName = "plate_y")
    private int plateY;
    @DatabaseField(columnName = "plate_z")
    private int plateZ;
    @DatabaseField(columnName = "world")
    private String worldName;
    @DatabaseField(columnName = "minX")
    private int minX;
    @DatabaseField(columnName = "minY")
    private int minY;
    @DatabaseField(columnName = "minZ")
    private int minZ;
    @DatabaseField(columnName = "maxX")
    private int maxX;
    @DatabaseField(columnName = "maxY")
    private int maxY;
    @DatabaseField(columnName = "maxZ")
    private int maxZ;
    
    public HillModel(@NotNull String name, Location plate, @NotNull Location pos1, @NotNull Location pos2) {
        this.name = name;
        splitCoordinates(plate, pos1, pos2);
    }
    
    public Location getMinLocation() {
        return new Location(getWorld(), minX, minY, minZ);
    }
    
    public Location getMaxLocation() {
        return new Location(getWorld(), maxX, maxY, maxZ);
    }
    
    public Location getPlateLocation() {
        return new Location(getWorld(), plateX, plateY, plateZ);
    }
    
    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }
    
    private void splitCoordinates(Location plate, Location pos1, Location pos2) {
        this.plateX = plate.getBlockX();
        this.plateY = plate.getBlockY();
        this.plateZ = plate.getBlockZ();
        
        int x1 = pos1.getBlockX();
        int x2 = pos2.getBlockX();
        int y1 = pos1.getBlockY();
        int y2 = pos2.getBlockY();
        int z1 = pos1.getBlockZ();
        int z2 = pos2.getBlockZ();
        
        this.minX = x1 < x2 ? x1 : x2;
        this.maxX = x1 > x2 ? x1 : x2;
        this.minY = y1 < y2 ? y1 : y2;
        this.maxY = y1 > y2 ? y1 : y2;
        this.minZ = z1 < z2 ? z1 : z2;
        this.maxZ = z2 > z1 ? z2 : z1;
    }
    
    public boolean isInRange(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        
        if(minX > x  || x > maxX) {
            return false;
        }
        
        if(minY > y || y > maxY) {
            return false;
        }
        
        if(minZ > z || z > maxZ) {
            return false;
        }
        
        return true;
    }
}
