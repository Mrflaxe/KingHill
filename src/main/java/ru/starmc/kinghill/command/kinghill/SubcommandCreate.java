package ru.starmc.kinghill.command.kinghill;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.OmnipotentSubcommand;
import ru.soknight.lib.configuration.Messages;
import ru.starmc.kinghill.provider.HillProvider;
import ru.starmc.kinghill.session.CreationSessionManager;

public class SubcommandCreate extends OmnipotentSubcommand {

    private final Messages messages;
    private final CreationSessionManager sessionManager;
    private final HillProvider hillProvider;
    
    public SubcommandCreate(Messages messages, CreationSessionManager sessionManager, HillProvider hillProvider) {
        super("kinghill.command.create", 1, messages);
        
        this.messages = messages;
        this.sessionManager = sessionManager;
        this.hillProvider = hillProvider;
    }

    @Override
    protected void executeCommand(@NotNull CommandSender sender, @NotNull CommandArguments args) {
        String hillId = args.get(0);
        
        if(hillProvider.isExist(hillId)) {
            messages.sendFormatted(sender, "creation.error.already-exist", "%name%", hillId);
            return;
        }
        
        if(containsSpecidalCharacters(hillId)) {
            messages.sendFormatted(sender, "creation.error.contains-special-characters", "%name%", hillId);
        }
        
        sessionManager.openSession((Player) sender, hillId);
        
        List<String> messages = this.messages.getColoredList("creation.select-plate");
        messages.forEach(message -> sender.sendMessage(message));
    }
    
    private boolean containsSpecidalCharacters(String string) {
        Pattern checkPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match = checkPattern.matcher(string);
        
        return match.find();
    }

}
