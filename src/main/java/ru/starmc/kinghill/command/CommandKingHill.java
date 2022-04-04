package ru.starmc.kinghill.command;

import ru.soknight.lib.command.preset.ModifiedDispatcher;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.KingHill;
import ru.starmc.kinghill.command.kinghill.SubcommandCreate;
import ru.starmc.kinghill.command.kinghill.SubcommandReload;
import ru.starmc.kinghill.provider.HillProvider;
import ru.starmc.kinghill.session.CreationSessionManager;

public class CommandKingHill extends ModifiedDispatcher {

    public CommandKingHill(Messages messages, CreationSessionManager sessionManager, HillProvider hillProvider, KingHill plugin) {
        super("kinghill", messages);
        
        super.setExecutor("create", new SubcommandCreate(messages, sessionManager, hillProvider));
        super.setExecutor("reload", new SubcommandReload(messages, plugin));
        
        super.register(plugin);
    }

}
