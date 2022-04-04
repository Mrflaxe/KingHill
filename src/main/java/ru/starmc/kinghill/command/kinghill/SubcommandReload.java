package ru.starmc.kinghill.command.kinghill;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.PermissibleSubcommand;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.KingHill;

public class SubcommandReload extends PermissibleSubcommand {

    private final Messages messages;
    private final KingHill plugin;
    
    public SubcommandReload(Messages messages, KingHill plugin) {
        super("kinghill.command.reload", messages);
        
        this.messages = messages;
        this.plugin = plugin;
    }

    @Override
    protected void executeCommand(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        plugin.refreshConfigs();
        messages.getAndSend(sender, "reload");
    }

}
