package ru.starmc.kinghill.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ru.soknight.lib.configuration.Configuration;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.database.model.HillModel;
import ru.starmc.kinghill.provider.HillProvider;
import ru.starmc.kinghill.time.TimeCounter;

public class KingHillListener implements Listener {

    private final Configuration config;
    private final Messages messages;
    private final HillProvider hillProvider;
    private final TimeCounter timeCounter;
    private final Plugin plugin;
    
    public KingHillListener(Configuration config, Messages messages, HillProvider hillProvider, TimeCounter timeCounter, JavaPlugin plugin) {
        this.config = config;
        this.messages = messages;
        this.hillProvider = hillProvider;
        this.timeCounter = timeCounter;
        this.plugin = plugin;
        
        register();
    }
    
    @EventHandler
    public void onPressurePlateActivate(PlayerInteractEvent event) {
        if(event.getAction() != Action.PHYSICAL) {
            return;
        }
        
        Block block = event.getClickedBlock();
        String stringType = block.getType().toString().toLowerCase();
        
        if(!stringType.endsWith("_plate")) {
            return;
        }
        
        HillModel hill = hillProvider.getHill(block.getLocation());
        
        if(hill == null) {
            return;
        }
        
        event.setCancelled(true);
        Player player = event.getPlayer();
        
        // If player is already a king no matter to make him again king
        if(timeCounter.isActiveKing(player)) {
           return;
        }
        
        // check for enought players in the range
        
        List<Player> playersIn = plugin.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> hill.isInRange(onlinePlayer.getLocation()))
                .collect(Collectors.toList());

        int required = config.getInt("players-required");
        
        // and sending a message if players are not enought
        if(playersIn.size() < required) {
            String formatted = messages.getFormatted("kinghill.not-enough-players", "%count%", required);
            player.sendActionBar(formatted);
            return;
        }
        
        timeCounter.runCounter(player, hill);
    }
    
    @EventHandler
    public void onKingHillBreake(BlockBreakEvent event) {
        Block block = event.getBlock();
        HillModel hill = hillProvider.getHill(block.getLocation());
        
        if(hill == null) {
            return;
        }
        
        Player player = event.getPlayer();
        hillProvider.deleteHill(hill);
        
        messages.sendFormatted(player, "delete", "%name%", hill.getName());
    }
    
    private void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
