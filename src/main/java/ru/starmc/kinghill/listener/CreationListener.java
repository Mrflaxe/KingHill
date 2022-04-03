package ru.starmc.kinghill.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.provider.HillProvider;
import ru.starmc.kinghill.session.CreationSession;
import ru.starmc.kinghill.session.CreationSessionManager;

public class CreationListener implements Listener {

    private final Messages messages;
    private final CreationSessionManager creationSessionManager;
    private final HillProvider hillProvider;
    
    public CreationListener(CreationSessionManager creationSessionManager, Messages messages, HillProvider hillProvider, JavaPlugin plugin) {
        this.messages = messages;
        this.creationSessionManager = creationSessionManager;
        this.hillProvider = hillProvider;
        
        register(plugin);
    }
    
    public void onBlockInterract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        
        if(event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        
        CreationSession creationSession = creationSessionManager.getSession(player);
        
        if(creationSession == null) {
            return;
        }
        
        event.setCancelled(true);
        Block block = event.getClickedBlock();
        
        if(action == Action.LEFT_CLICK_BLOCK) {
            Location plateLocation = creationSession.getPlateLocation();
            
            if(plateLocation == null) {
                Material type = block.getType();
                String stringType = type.name().toLowerCase();
                
                if(!stringType.endsWith("_plate")) {
                    messages.getAndSend(player, "creation.error.not-plate");
                    return;
                }
                
                creationSession.setPlateLocation(block.getLocation());
                
                List<String> messages = this.messages.getColoredList("creation.select-area");
                messages.forEach(message -> player.sendMessage(message));
                
                return;
            }
            
            if(plateLocation != null) {
                if(creationSession.getPos1() == null) {
                    List<String> messages = this.messages.getColoredList("creation.selected-pos1");
                    messages.forEach(message -> player.sendMessage(message));
                } else {
                    messages.getAndSend(player, "creation.reselected-pos1");
                }
                
                Location newPos1Location = block.getLocation();
                creationSession.setPos1(newPos1Location);
                return;
            }
        }
        
        if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Location pos1 = creationSession.getPos1();
            
            if(pos1 == null) {
                messages.getAndSend(player, "creation.error.pos1-first");
                return;
            }
            
            Location newPos2Location = block.getLocation();
            creationSession.setPos2(newPos2Location);
            
            hillProvider.createHill(creationSession);
            creationSessionManager.closeSession(player);
            
            messages.sendFormatted(player, "creation.succes", "%name%", creationSession.getId());
            return;
        }
    }
    
    private void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
