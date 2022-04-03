package ru.starmc.kinghill.provider;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import ru.starmc.kinghill.database.DatabaseManager;
import ru.starmc.kinghill.database.model.HillModel;
import ru.starmc.kinghill.session.CreationSession;

public class HillProvider {

    private final DatabaseManager databaseManager;
    
    private final Map<Location, HillModel> hills; //cache
    
    public HillProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.hills = new HashMap<>();
    }
    
    public void saveData() {
        hills.values().stream()
                .forEach(hill -> databaseManager.saveHill(hill));
    }
    
    public HillModel getHill(Location location) {
        if(hills.containsKey(location)) {
            return hills.get(location);
        }
        
        HillModel hill = databaseManager.getHillByTop(location).join();
        
        if(hill != null) {
            hills.put(location, hill);
        }
        
        return hill;
    }
    
    public boolean isExist(String id) {
        return databaseManager.getHill(id) != null;
    }
    
    public void createHill(CreationSession creationSession) {
        String name = creationSession.getId();
        Location plate = creationSession.getPlateLocation();
        Location pos1 = creationSession.getPos1();
        Location pos2 = creationSession.getPos2();
        
        HillModel hill = new HillModel(name, plate, pos1, pos2);
        databaseManager.saveHill(hill);
    }
}
