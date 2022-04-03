package ru.starmc.kinghill.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.standalone.PlayerOnlyCommand;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.session.CreationSessionManager;

public class CommandCancel extends PlayerOnlyCommand {
    
    private final Messages messages;
    private final CreationSessionManager creationSessionManager;
    
    public CommandCancel(Messages messages, CreationSessionManager creationSessionManager, JavaPlugin plugin) {
        super("cancel", "kinghill.command.cancel", messages);
        
        this.messages = messages;
        this.creationSessionManager = creationSessionManager;
        
        super.register(plugin);
    }

    @Override
    protected void executeCommand(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        Player player = (Player) sender;
        
        if(!creationSessionManager.hasSession(player)) {
            return;
        }
        
        creationSessionManager.closeSession(player);
        messages.getAndSend(sender, "creation.cancel");
    }
}
