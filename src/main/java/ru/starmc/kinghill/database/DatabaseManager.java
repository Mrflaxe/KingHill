package ru.starmc.kinghill.database;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import ru.soknight.lib.database.Database;
import ru.soknight.lib.executable.quiet.AbstractQuietExecutor;
import ru.starmc.kinghill.database.model.HillModel;
import ru.starmc.kinghill.database.model.ProfileModel;

public class DatabaseManager extends AbstractQuietExecutor {

    private final ConnectionSource connection;
    
    private final Dao<HillModel, String> hillDao;
    private final Dao<ProfileModel, String> profileDao;
    
    public DatabaseManager(Database database, Plugin plugin) throws SQLException {
        this.connection = database.establishConnection();
        
        this.hillDao = DaoManager.createDao(connection, HillModel.class);
        this.profileDao = DaoManager.createDao(connection, ProfileModel.class);
        
        super.useDatabaseThrowableHandler(plugin);
        super.useCachedThreadPoolAsyncExecutor();
    }
    
    @Override
    public void shutdown() {
        try {
            if(connection != null) connection.close();
        } catch (Exception ignored) {}
    }
    
    /**
     *      Profile section
     */
    
    public CompletableFuture<Void> saveProfile(ProfileModel profile) {
        return runQuietlyAsync(() -> profileDao.createOrUpdate(profile));
    }
    
    public CompletableFuture<ProfileModel> getProfile(String name) {
        return supplyQuietlyAsync(() -> profileDao.queryForId(name));
    }
    
    public CompletableFuture<List<ProfileModel>> getAllProfiles() {
        return supplyQuietlyAsync(() -> profileDao.queryForAll());
    }
    
    /**
     *      Hill section
     */
    
    public CompletableFuture<Void> saveHill(HillModel hill) {
        return runQuietlyAsync(() -> hillDao.createOrUpdate(hill));
    }
    
    public CompletableFuture<HillModel> getHill(String id) {
        return supplyQuietlyAsync(() -> hillDao.queryForId(id));
    }
    
    public CompletableFuture<HillModel> getHillByTop(Location location) {
        return supplyQuietlyAsync(() -> hillDao.queryBuilder().where()
                .eq("plate_x", location.getBlockX()).and()
                .eq("plate_y", location.getBlockY()).and()
                .eq("plate_z", location.getBlockZ()).queryForFirst());
    }
    
    public CompletableFuture<Void> deleteHill(HillModel hill) {
        return runQuietlyAsync(() -> hillDao.delete(hill));
    }
}
