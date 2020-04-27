package net.neszku.patch_tracker.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public interface ICommandContext {


    /**
     * Gets the message that invoked command
     *
     * @return message that invoked command
     */
    Message getMessage();

    /**
     * Gets the argument with certain index
     *
     * @param index index of the argument
     * @throws IllegalStateException if argument doesn't exist
     * @return argument
     */
    String arg(int index);

    /**
     * Gets the list of arguments
     *
     * @return list of arguments
     */
    List<String> args();

    /**
     * Gets the size of the arguments
     *
     * @return size of the arguments
     */
    int size();

    /**
     * Gets the {@link TextChannel} from which
     * command has been invoked
     *
     * @return {@link TextChannel}
     */
    TextChannel getChannel();

    /**
     * Gets the {@link User} that invoked the command
     *
     * @return {@link User} that invoked the command
     */
    User getSender();

}
