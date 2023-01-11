package ru.starmc.kinghill.time;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.destroystokyo.paper.Title;

import ru.soknight.lib.configuration.Configuration;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.database.model.HillModel;
import ru.starmc.kinghill.database.model.ProfileModel;
import ru.starmc.kinghill.formatter.TimeFormatter;
import ru.starmc.kinghill.provider.ProfileProvider;

public class TimeCounter {

    private final JavaPlugin plugin;
    private final Messages messages;
    private final Configuration config;
    private final ProfileProvider profileProvider;
    
    private final Map<Player, Integer> timeData;
    private final Map<Player, BukkitTask> activeKingTasks;
    private final Map<Player, HillModel> claimedHills;
    
    public TimeCounter(JavaPlugin plugin, Messages messages, Configuration config, ProfileProvider profileProvider) {
        this.plugin = plugin;
        this.messages = messages;
        this.config = config;
        this.profileProvider = profileProvider;
        
        this.timeData = new HashMap<>();
        this.activeKingTasks = new HashMap<>();
        this.claimedHills = new HashMap<>();
        
        runDailyRewardUpdate();
    }
    
    public void runCounter(Player player, HillModel hill) {
        Location plateLocation = hill.getPlateLocation();
        
        if(!profileProvider.hasProfile(player)) {
            profileProvider.createProfile(player);
        }
        
        if(!claimedHills.containsKey(player)) {
            claimedHills.put(player, hill);
        }
        
        if(!timeData.containsKey(player)) {
            timeData.put(player, 0);
        }
        
        ProfileModel profile = profileProvider.getProfile(player);
        
        activeKingTasks.put(player, plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            Location playerLocation = player.getLocation();
            
            TimeFormatter formatter = new TimeFormatter();
            boolean isOnPlate = isOnPlate(playerLocation, plateLocation);
            
            // If player no longer king of the hill
            if(!isOnPlate) {
                updateData(player);
                
                String message = messages.getColoredString("kinghill.result");
                message = formatter.format(timeData.get(player), message);
                player.sendMessage(message);
                
                Title title = new Title("");
                player.sendTitle(title);
                
                timeData.remove(player);
                claimedHills.remove(player);
                closeTask(player);
                
                return;
            }
            
            // If players not enough
            List<Player> playersIn = plugin.getServer().getOnlinePlayers().stream()
                    .filter(onlinePlayer -> hill.isInRange(onlinePlayer.getLocation()))
                    .collect(Collectors.toList());
            
            int required = config.getInt("players-required");
            
            if(playersIn.size() < required) {
                String formatted = messages.getFormatted("kinghill.not-enough-players", "%count%", required);
                player.sendActionBar(formatted);
                return;
            }
            
            int size = claimedHills.values().stream()
                    .filter(value -> value.equals(hill))
                    .collect(Collectors.toList())
                    .size();
            
            // If 2 or more players at one time at the top of hill
            if(size > 1) {
                String titleMessage = messages.get("kinghill.only-one-can.title");
                Title title = new Title(titleMessage);
                
                player.sendTitle(title);
                messages.getAndSend(player, "kinghill.only-one-can.message");
                
                return;
            }
            
            int newValue = timeData.get(player) + 1;
            timeData.put(player, newValue);
            profile.addTime(1);
                
            if(profile.isRewardAvailable()) {
                int req = config.getInt("daily-reward-required");
                
                if(newValue >= req) {
                    giveReward(player);
                    messages.getAndSend(player, "kinghill.daily-reward");
                    profile.setRewardAvailable(false);
                }
            }
            
            String message = messages.getColoredString("kinghill.title");
            message = formatter.format(timeData.get(player), message);
            
            Title title = new Title(message);
            player.sendTitle(title);
        }, 20, 20));
        
        
    }
    
    public boolean isActiveKing(Player player) {
        return activeKingTasks.containsKey(player);
    }
    
    private void giveReward(Player player) {
        List<String> rewardCommands = config.getList("reward");
        
        rewardCommands.forEach(command -> {
            command = command.replace("%player%", player.getName());
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
        });
    }
    
    private void runDailyRewardUpdate() {
        Calendar currentCalendar = new GregorianCalendar();
        Calendar nextDayCalendar = new GregorianCalendar();
        
        nextDayCalendar.add(Calendar.DAY_OF_YEAR, 1);
        nextDayCalendar.set(Calendar.SECOND, 0);
        nextDayCalendar.set(Calendar.MINUTE, 0);
        nextDayCalendar.set(Calendar.HOUR, 0);

        long dif = nextDayCalendar.getTime().getTime() - currentCalendar.getTime().getTime();
        long seconds = dif / 1000;
        
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            profileProvider.resetRewardAvailability();
        }, seconds * 20, 24 * 60 * 60 * 20);
    }
    
    private void updateData(Player player) {
        ProfileModel profile = profileProvider.getProfile(player);
        int time;

        if(!timeData.containsKey(player)) {
            time = 0;
        } else {
            time = timeData.get(player);
        }

        int maxTime = profile.getMaxTime();

        if(maxTime < time) {
            profile.setMaxTime(time);
        }
    }
    
    private boolean isOnPlate(Location playerLocation, Location plateLocation) {
        float minX = plateLocation.getBlockX() - 0.25f;
        float maxX = plateLocation.getBlockX() + 1.25f;
        float minZ = plateLocation.getBlockZ() - 0.25f;
        float maxZ = plateLocation.getBlockZ() + 1.25f;
        
        double x = playerLocation.getX();
        double z = playerLocation.getZ();
        
        if(minX > x || x > maxX) return false;
        if(minZ > z || z > maxZ) return false;
        
        return true;
    }
    
    private void closeTask(Player player) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            activeKingTasks.get(player).cancel();
            activeKingTasks.remove(player);
        });
    }
}
