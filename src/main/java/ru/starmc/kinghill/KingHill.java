package ru.starmc.kinghill;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import ru.soknight.lib.configuration.Configuration;
import ru.soknight.lib.configuration.Messages;
import ru.soknight.lib.database.Database;
import ru.starmc.kinghill.command.CommandCancel;
import ru.starmc.kinghill.command.CommandKingHill;
import ru.starmc.kinghill.database.DatabaseManager;
import ru.starmc.kinghill.database.model.HillModel;
import ru.starmc.kinghill.database.model.ProfileModel;
import ru.starmc.kinghill.listener.CreationListener;
import ru.starmc.kinghill.listener.KingHillListener;
import ru.starmc.kinghill.placeholder.PlaceholdersHook;
import ru.starmc.kinghill.provider.HillProvider;
import ru.starmc.kinghill.provider.ProfileProvider;
import ru.starmc.kinghill.session.CreationSessionManager;
import ru.starmc.kinghill.time.TimeCounter;

public class KingHill extends JavaPlugin {

    private Configuration config;
    private Messages messages;
    
    private DatabaseManager databaseManager;
    private HillProvider hillProvider;
    private ProfileProvider profileProvider;
    private CreationSessionManager sessionManager;
    private TimeCounter TimeCounter;
    
    @Override
    public void onEnable() {
        refreshConfigs();
        
        if(this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholdersHook(profileProvider, config).register();
        } //TODO log that placeholderAPI is disabled
        
        try {
            Database database = new Database(this, config)
                    .createTable(HillModel.class)
                    .createTable(ProfileModel.class);
            
            this.databaseManager = new DatabaseManager(database, this);
        } catch (SQLException ex) {
            getLogger().severe("Failed to establish a database connection: " + ex.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        } catch (Exception ex) {
            getLogger().severe("Failed to initialize the database!");
            ex.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        this.hillProvider = new HillProvider(databaseManager);
        this.profileProvider = new ProfileProvider(databaseManager);
        this.sessionManager = new CreationSessionManager();
        this.TimeCounter = new TimeCounter(this, messages, config, profileProvider);
        
        registerCommands();
        registerListeners();
    }
    
    @Override
    public void onDisable() {
        if(profileProvider != null) {
            profileProvider.saveData();
        }
        
        if(hillProvider != null) {
            hillProvider.saveData();
        }
    }

    private void refreshConfigs() {
        if(config == null) this.config = new Configuration(this, "config.yml");
        config.refresh();
        
        if(messages == null) this.messages = new Messages(this, "messages.yml");
        messages.refresh();
    }
    
    private void registerListeners() {
        new CreationListener(sessionManager, messages, hillProvider, this);
        new KingHillListener(config, messages, hillProvider, TimeCounter, this);
    }
    
    private void registerCommands() {
        new CommandKingHill(messages, sessionManager, hillProvider, this);
        new CommandCancel(messages, sessionManager, this);
    }
}
