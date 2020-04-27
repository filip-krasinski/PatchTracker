package net.neszku.patch_tracker.helpers;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionHelper {

    /**
     * Checks whether has permissions on channel
     *
     * @param channel channel to check permissions on
     * @param permissions permissions to check
     * @return set of not owned permissions
     */
    public static Set<Permission> checkPermissions(TextChannel channel, Collection<Permission> permissions) {
        Guild  guild  = channel.getGuild();
        Member member = guild.getSelfMember();
        return permissions.stream()
                .filter(member::hasPermission)
                .collect(Collectors.toSet());
    }

}
