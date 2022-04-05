package ru.starmc.kinghill.placeholder;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import ru.soknight.lib.configuration.Configuration;
import ru.starmc.kinghill.placeholder.hologramDipslays.NamePlaceholderReplacer;
import ru.starmc.kinghill.placeholder.hologramDipslays.TimePlaceholderReplacer;
import ru.starmc.kinghill.provider.ProfileProvider;

public class HolographicDisplaysPlaceholderDispatcher {
    
    private final Configuration config;
    private final ProfileProvider profileProvider;
    private final JavaPlugin plugin;
    
    public HolographicDisplaysPlaceholderDispatcher(Configuration config, ProfileProvider profileProvider, JavaPlugin plugin) {
        this.config = config;
        this.profileProvider = profileProvider;
        this.plugin = plugin;
    }
    
    public void registerPlaceholders() {
        int refreshTime = config.getInt("holographic-displays-update-time", 1);
        
        for(int i = 1; i < 6; i++) {
            NamePlaceholderReplacer allTimeNameReplacer = new NamePlaceholderReplacer(profileProvider, DataType.ALLTIME, i);
            NamePlaceholderReplacer maxTimeNameReplacer = new NamePlaceholderReplacer(profileProvider, DataType.MAXTIME, i);
            TimePlaceholderReplacer allTimeValueReplacer = new TimePlaceholderReplacer(config, profileProvider, DataType.ALLTIME, i);
            TimePlaceholderReplacer maxTimeValueReplacer = new TimePlaceholderReplacer(config, profileProvider, DataType.MAXTIME, i);
            
            HologramsAPI.registerPlaceholder(plugin, "%kh_name_top" + i + "%", refreshTime, allTimeNameReplacer);
            HologramsAPI.registerPlaceholder(plugin, "%kh_maxname_top" + i + "%", refreshTime, maxTimeNameReplacer);
            HologramsAPI.registerPlaceholder(plugin, "%kh_time_top" + i + "%", refreshTime, allTimeValueReplacer);
            HologramsAPI.registerPlaceholder(plugin, "%kh_maxtime_top" + i + "%", refreshTime, maxTimeValueReplacer);
        }
    }
}
