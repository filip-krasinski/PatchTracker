package net.neszku.patch_tracker.command.impl;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.neszku.patch_tracker.command.ICommandContext;

import java.util.List;

public class CommandContext implements ICommandContext {

    private final Message message;
    private final List<String> args;
    private final TextChannel channel;
    private final User sender;

    public CommandContext(Message message, List<String> args, TextChannel channel, User sender) {
        this.message = message;
        this.args = args;
        this.channel = channel;
        this.sender = sender;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String arg(int arg) {
        return args.get(arg);
    }

    @Override
    public List<String> args() {
        return args;
    }

    @Override
    public int size() {
        return args.size();
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public User getSender() {
        return sender;
    }

}
