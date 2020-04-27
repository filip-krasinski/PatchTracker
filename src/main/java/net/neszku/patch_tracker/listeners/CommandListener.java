package net.neszku.patch_tracker.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.neszku.patch_tracker.Config;
import net.neszku.patch_tracker.PatchTracker;
import net.neszku.patch_tracker.command.Command;
import net.neszku.patch_tracker.command.ICommandService;
import net.neszku.patch_tracker.command.impl.CommandContext;
import net.neszku.patch_tracker.helpers.PermissionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandListener extends ListenerAdapter {

    private static final Set<Permission> REQUIRED_PERMISSIONS = new HashSet<>(Arrays.asList(
            Permission.MESSAGE_EMBED_LINKS,
            Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_MANAGE
    ));

    private final PatchTracker instance;

    public CommandListener(PatchTracker instance) {
        this.instance = instance;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Message     message = event.getMessage();
        String      raw = message.getContentRaw();
        String[]    rawParts = raw.split(" ");

        if (event.getAuthor().isBot()) {
            return;
        }

        if (raw.isEmpty()) {
            return;
        }

        if (!raw.startsWith(Config.COMMAND_PREFIX)) {
            return;
        }

        ICommandService commandService = instance.getCommandService();
        Command command = commandService.getCommand(rawParts[0].substring(Config.COMMAND_PREFIX.length()));

        if (command == null) {
            System.out.println("null");
            return;
        }

        Member self = channel.getGuild().getSelfMember();
        if (!self.hasPermission(channel, Permission.MESSAGE_WRITE)) {
            return;
        }

        Set<Permission> notOwned = PermissionHelper.checkPermissions(channel, REQUIRED_PERMISSIONS);
        if (!notOwned.isEmpty()) {
            channel.sendMessage("I require permissions **" + notOwned.toString() + "**").queue();
            return;
        }

        List<String> args = new ArrayList<>();
        String[] input = message.getContentRaw().split("\\s+", 2);
        if (input.length == 2) {
            args.addAll(Arrays.asList(input[1].split(" +")));
        }

        command.execute(new CommandContext(message, args, channel, event.getAuthor()));
    }
}