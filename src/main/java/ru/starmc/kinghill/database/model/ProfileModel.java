package ru.starmc.kinghill.database.model;

import com.j256.ormlite.field.DatabaseField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ProfileModel {
    
    @DatabaseField(columnName = "players_name", id = true)
    private String name;
    @DatabaseField(columnName = "time_sec")
    private int time;
    @Setter
    @DatabaseField(columnName = "max_time_at_once")
    private int maxTime;
    @Setter
    @DatabaseField(columnName = "reward_available")
    private boolean rewardAvailable;
    
    public void addTime(int value) {
        this.time = time + value;
    }
}
