package ru.starmc.kinghill.command;

import org.bukkit.plugin.java.JavaPlugin;

import ru.soknight.lib.command.preset.ModifiedDispatcher;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.command.kinghill.SubcommandCreate;
import ru.starmc.kinghill.provider.HillProvider;
import ru.starmc.kinghill.session.CreationSessionManager;

public class CommandKingHill extends ModifiedDispatcher {

    public CommandKingHill(Messages messages, CreationSessionManager sessionManager, HillProvider hillProvider, JavaPlugin plugin) {
        super("kinghill", messages);
        
        super.setExecutor("create", new SubcommandCreate(messages, sessionManager, hillProvider));
        
        super.register(plugin);
    }

}
