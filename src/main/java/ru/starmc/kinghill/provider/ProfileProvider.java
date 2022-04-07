package ru.starmc.kinghill.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import ru.starmc.kinghill.database.DatabaseManager;
import ru.starmc.kinghill.database.model.ProfileModel;
import ru.starmc.kinghill.placeholder.DataType;

public class ProfileProvider {

    private final DatabaseManager databaseManager;
    
    private final Map<String, ProfileModel> profiles;
    
    public ProfileProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.profiles = new HashMap<>();
    }
    
    public void saveData() {
        profiles.values().stream()
                .forEach(profile -> databaseManager.saveProfile(profile));
    }
    
    public ProfileModel getProfile(Player player) {
        return getProfile(player.getName());
    }
    
    public boolean hasProfile(Player player) {
        String name = player.getName();
        
        if(profiles.containsKey(name)) {
            return true;
        }
        
        ProfileModel profile = databaseManager.getProfile(name).join();
        
        if(profile != null) {
            profiles.put(name, profile);
            return true;
        }
        
        return false;
    }
    
    public ProfileModel getProfile(String name) {
        if(profiles.containsKey(name)) {
            return profiles.get(name);
        }
        
        ProfileModel profile = databaseManager.getProfile(name).join();
        profiles.put(name, profile);
        
        return profile;
    }
    
    public List<ProfileModel> getAllProfiles() {
        List<ProfileModel> activeProfiles = this.profiles.entrySet().stream()
                .map(Entry::getValue)
                .collect(Collectors.toList());
        
        List<ProfileModel> profiles = databaseManager.getAllProfiles().join();
        List<ProfileModel> totalCollection = new ArrayList<>();
        
        totalCollection.addAll(profiles);
        List<String> activeProfilesNames = activeProfiles.stream()
                .map(ProfileModel::getName)
                .collect(Collectors.toList());
        
        for (ProfileModel profileModel : profiles) {
            if(activeProfilesNames.contains(profileModel.getName())) {
                totalCollection.remove(profileModel);
                continue;
            }
        }
        
        totalCollection.addAll(activeProfiles);
        
        return totalCollection;
    }
    
    public ProfileModel createProfile(Player player) {
        ProfileModel newProfile = new ProfileModel(player.getName(), 0, 0, true);
        profiles.put(player.getName(), newProfile);
        databaseManager.saveProfile(newProfile);
        
        return newProfile;
    }
    
    public void resetRewardAvailability() {
        profiles.values().forEach(profile -> profile.setRewardAvailable(true));
    }
    
    public List<ProfileModel> getTop5Profiles(DataType type) {
        List<ProfileModel> profiles = getAllProfiles();
        List<ProfileModel> top5 = new ArrayList<>();
        
        int pos;
        ProfileModel temp;
        
        for(int i = 0; i < profiles.size(); i++) {
            pos = i;
            
            for (int j = i+1; j < profiles.size(); j++) {
                
                if(type == DataType.ALLTIME) {
                    if (profiles.get(j).getTime() > profiles.get(pos).getTime()) {
                        pos = j;
                        continue;
                    }
                }
                
                if(type == DataType.MAXTIME) {
                    if (profiles.get(j).getMaxTime() > profiles.get(pos).getMaxTime()) {
                        pos = j;
                        continue;
                    }
                }

            }
            
            temp = profiles.get(pos);            //swap the current element with the minimum element
            profiles.set(pos, profiles.get(i));
            profiles.set(i, temp);
            
            top5.add(temp);
        }
        
        return top5;
    }
}
