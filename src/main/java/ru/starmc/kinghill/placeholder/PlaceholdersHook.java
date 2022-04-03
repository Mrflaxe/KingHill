package ru.starmc.kinghill.placeholder;

import java.util.List;

import org.bukkit.entity.Player;

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
    
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        for(int i = 1; i < 6; i++) {
            String allTimePlacePlaceholder = "%kh_time_top" + i + "%";
            String namePlcaeholder = "%kh_name_top" + i + "%";
            String maxTimePlaceholder = "%kh_maxtime_top" + i + "%";

            List<ProfileModel> top5AllTime = profileProvider.getTop5Profiles(true);
            List<ProfileModel> top5MaxTime = profileProvider.getTop5Profiles(false);
            
            if(params.equalsIgnoreCase(allTimePlacePlaceholder)) {
                ProfileModel profile = top5AllTime.get(i);
                int time = profile.getTime();
                
                String pattern = config.getString("placeholder-time-pattern");
                TimeFormatter formatter = new TimeFormatter();
                
                return formatter.format(time, pattern);
            }
            
            if(params.equalsIgnoreCase(maxTimePlaceholder)) {
                ProfileModel profile = top5MaxTime.get(i);
                int time = profile.getTime();
                
                String pattern = config.getString("placeholder-time-pattern");
                TimeFormatter formatter = new TimeFormatter();
                
                return formatter.format(time, pattern);
            }
            
            if(params.equalsIgnoreCase(namePlcaeholder)) {
                ProfileModel profile = top5AllTime.get(i);
                return profile.getName();
            }
        }
        
        return null;
    }

    @Override
    public String getIdentifier() {
        return "kinghill";
    }

    @Override
    public String getAuthor() {
        return "Mrflaxe";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
