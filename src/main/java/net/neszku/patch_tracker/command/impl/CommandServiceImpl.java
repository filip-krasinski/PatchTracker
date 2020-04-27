package net.neszku.patch_tracker.command.impl;

import net.neszku.patch_tracker.command.Command;
import net.neszku.patch_tracker.command.ICommandService;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class CommandServiceImpl implements ICommandService {

    private final Set<Command> commands = new HashSet<>();

    @Override
    public Set<Command> getCommands() {
        return new HashSet<>(commands);
    }

    @Override
    public @Nullable Command getCommand(String identifier) {
        return commands.stream()
                .filter(command ->
                    command.getName().equalsIgnoreCase(identifier) ||
                    command.getAliases().contains(identifier)
                )
                .findAny()
                .orElse(null);
    }

    @Override
    public void registerCommand(Command command) {
        commands.add(command);
    }
}
