package net.neszku.patch_tracker.command;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.neszku.patch_tracker.command.impl.CommandContext;

import java.util.List;

public abstract class Command {

    private final String name;
    private final List<String> aliases;
    private final MessageEmbed usage;

    public Command(String name, List<String> aliases, MessageEmbed usage) {
        this.name = name;
        this.aliases = aliases;
        this.usage = usage;
    }

    /**
     * Gets the name of this command
     *
     * @return name of this command
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of aliases for this command
     *
     * @return list of aliases for this command
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Gets the {@link MessageEmbed} that represents
     * usage of this command
     *
     * @return usage of this command
     */
    public MessageEmbed getUsage() {
        return usage;
    }

    /**
     * Called upon execution of this command
     *
     * @param context the context of this command
     */
    public abstract void execute(ICommandContext context);
}