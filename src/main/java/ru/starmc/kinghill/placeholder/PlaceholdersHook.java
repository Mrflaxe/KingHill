package ru.starmc.kinghill.placeholder;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.AllArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import ru.soknight.lib.configuration.Configuration;
import ru.starmc.kinghill.database.model.ProfileModel;
import ru.starmc.kinghill.formatter.TimeFormatter;
import ru.starmc.kinghill.provider.ProfileProvider;

@AllArgsConstructor
public class PlaceholdersHook extends PlaceholderExpansion {

    private final ProfileProvider profileProvider;
    private final Configuration config;
    private final JavaPlugin plugin;
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        for(int i = 1; i < 6; i++) {
            String allTimePlacePlaceholder = "time_top" + i;
            String allTimeNamePlcaeholder = "name_top" + i;
            String maxTimePlaceholder = "maxtime_top" + i;
            String maxTimeNamePlaceholder = "maxname_top" + i;

            List<ProfileModel> top5AllTime = profileProvider.getTop5Profiles(DataType.ALLTIME);
            List<ProfileModel> top5MaxTime = profileProvider.getTop5Profiles(DataType.MAXTIME);
            
            if(params.equalsIgnoreCase(allTimePlacePlaceholder)) {
                ProfileModel profile = top5AllTime.get(i - 1);
                int time = profile.getTime();
                
                String pattern = config.getString("placeholder-time-pattern");
                TimeFormatter formatter = new TimeFormatter();
                
                return formatter.format(time, pattern);
            }
            
            if(params.equalsIgnoreCase(maxTimePlaceholder)) {
                ProfileModel profile = top5MaxTime.get(i - 1);
                int time = profile.getTime();
                
                String pattern = config.getString("placeholder-time-pattern");
                TimeFormatter formatter = new TimeFormatter();
                
                return formatter.format(time, pattern);
            }
            
            if(params.equalsIgnoreCase(allTimeNamePlcaeholder)) {
                ProfileModel profile = top5AllTime.get(i - 1);
                return profile.getName();
            }
            
            if(params.equalsIgnoreCase(maxTimeNamePlaceholder)) {
                ProfileModel profile = top5MaxTime.get(i - 1);
                return profile.getName();
            }
        }
        
        return null;
    }

    @Override
    public String getIdentifier() {
        return "kh";
    }

    @Override
    public String getAuthor() {
        return "Mrflaxe";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
