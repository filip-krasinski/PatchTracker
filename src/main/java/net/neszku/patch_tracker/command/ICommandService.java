package net.neszku.patch_tracker.command;

import javax.annotation.Nullable;
import java.util.Set;

public interface ICommandService {

    /**
     * Returns the set of registered commands
     *
     * @return set of registered commands
     */
    Set<Command> getCommands();

    /**
     * Gets the command by name or by the alias
     *
     * @param identifier identifier of the command
     * @return {@link Command} if found otherwise null
     */
    @Nullable Command getCommand(String identifier);

    /**
     * Registers the command
     *
     * @param command command to be registered
     */
    void registerCommand(Command command);

}
