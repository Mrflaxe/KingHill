package ru.starmc.kinghill.placeholder.hologramDipslays;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholder;
import ru.soknight.lib.configuration.Configuration;
import ru.starmc.kinghill.database.model.ProfileModel;
import ru.starmc.kinghill.placeholder.DataType;
import ru.starmc.kinghill.provider.ProfileProvider;

public class NamePlaceholderReplacer implements GlobalPlaceholder {

    private final Configuration config;
    private final ProfileProvider profileProvider;
    private final DataType type;
    private final int position;
    private final int refreshInterval;
    
    public NamePlaceholderReplacer(Configuration config, ProfileProvider profileProvider, DataType type, int position, int refreshInterval) {
        this.config = config;
        this.profileProvider = profileProvider;
        this.type = type;
        this.position = position;
        this.refreshInterval = refreshInterval;
    }

    @Override
    public @Nullable String getReplacement(@Nullable String arg0) {
        List<ProfileModel> top = profileProvider.getTop5Profiles(type);
        String noneMessage = config.getString("no-data", "none");
        
        if(top.size() < position) {
            return noneMessage;
        }
        
        ProfileModel profile = top.get(position - 1);
        
        if(profile == null) {
            return noneMessage;
        }
        
        return profile.getName();
    }
    
    @Override
    public int getRefreshIntervalTicks() {
        return refreshInterval;
    }
}
