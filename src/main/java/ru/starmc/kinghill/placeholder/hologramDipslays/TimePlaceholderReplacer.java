package ru.starmc.kinghill.placeholder.hologramDipslays;

import java.util.List;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

import lombok.AllArgsConstructor;
import ru.soknight.lib.configuration.Configuration;
import ru.starmc.kinghill.database.model.ProfileModel;
import ru.starmc.kinghill.formatter.TimeFormatter;
import ru.starmc.kinghill.placeholder.DataType;
import ru.starmc.kinghill.provider.ProfileProvider;

@AllArgsConstructor
public class TimePlaceholderReplacer implements PlaceholderReplacer {

    private final Configuration config;
    private final ProfileProvider profileProvider;
    private final DataType type;
    private final int position;
    
    @Override
    public String update() {
        List<ProfileModel> top = profileProvider.getTop5Profiles(type);
        
        if(top.size() < position) {
            return "none";
        }
        
        ProfileModel profile = top.get(position - 1);
        
        if(profile == null) {
            return "none";
        }
        
        String pattern = config.getString("placeholder-time-pattern");
        TimeFormatter formatter = new TimeFormatter();
        
        return type.equals(DataType.ALLTIME) ? formatter.format(profile.getTime(), pattern) : formatter.format(profile.getMaxTime(), pattern);
    }
    
    
}
