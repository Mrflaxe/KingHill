package ru.starmc.kinghill.placeholder;

import org.bukkit.plugin.java.JavaPlugin;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import ru.soknight.lib.configuration.Configuration;
import ru.starmc.kinghill.placeholder.hologramDipslays.NamePlaceholderReplacer;
import ru.starmc.kinghill.placeholder.hologramDipslays.TimePlaceholderReplacer;
import ru.starmc.kinghill.provider.ProfileProvider;

public class HolographicDisplaysPlaceholderDispatcher {
    
    private final Configuration config;
    private final ProfileProvider profileProvider;
    
    private final HolographicDisplaysAPI holographicDisplaysAPI;
    
    public HolographicDisplaysPlaceholderDispatcher(Configuration config, ProfileProvider profileProvider, JavaPlugin plugin) {
        this.config = config;
        this.profileProvider = profileProvider;
        
        this.holographicDisplaysAPI = HolographicDisplaysAPI.get(plugin);
    }
    
    public void registerPlaceholders() {
        int refreshTime = config.getInt("holographic-displays-update-time", 1);
        
        for(int i = 1; i < 6; i++) {
            NamePlaceholderReplacer allTimeNameReplacer = new NamePlaceholderReplacer(config, profileProvider, DataType.ALLTIME, i, refreshTime);
            NamePlaceholderReplacer maxTimeNameReplacer = new NamePlaceholderReplacer(config, profileProvider, DataType.MAXTIME, i, refreshTime);
            TimePlaceholderReplacer allTimeValueReplacer = new TimePlaceholderReplacer(config, profileProvider, DataType.ALLTIME, i, refreshTime);
            TimePlaceholderReplacer maxTimeValueReplacer = new TimePlaceholderReplacer(config, profileProvider, DataType.MAXTIME, i, refreshTime);
            
            holographicDisplaysAPI.registerGlobalPlaceholder("kh_name_top" + i, allTimeNameReplacer);
            holographicDisplaysAPI.registerGlobalPlaceholder("kh_maxname_top" + i, maxTimeNameReplacer);
            holographicDisplaysAPI.registerGlobalPlaceholder("kh_time_top" + i, allTimeValueReplacer);
            holographicDisplaysAPI.registerGlobalPlaceholder("kh_maxtime_top" + i, maxTimeValueReplacer);
        }
    }
}
