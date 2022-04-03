package ru.starmc.kinghill.session;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class CreationSessionManager {
    
    private final Map<Player, CreationSession> sessions;
    
    public CreationSessionManager() {
        this.sessions = new HashMap<>();
    }
    
    public void openSession(Player player, String id) {
        CreationSession session = new CreationSession(id);
        sessions.put(player, session);
    }
    
    public CreationSession getSession(Player player) {
        if(!sessions.containsKey(player)) {
            return null;
        }
        
        return sessions.get(player);
    }
    
    public boolean hasSession(Player player) {
        return sessions.containsKey(player);
    }
    
    public void closeSession(Player player) {
        if(sessions.containsKey(player)) {
            sessions.remove(player);
        }
    }
}
