package net.neszku.patch_tracker.command.impl;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.neszku.patch_tracker.PatchTracker;
import net.neszku.patch_tracker.command.Command;
import net.neszku.patch_tracker.command.ICommandContext;
import net.neszku.patch_tracker.database.impl.Database;
import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.game.IGameService;
import net.neszku.patch_tracker.helpers.StringHelper;

import java.awt.Color;
import java.util.Arrays;

public class TrackCommand extends Command {

    public TrackCommand() {
        super("track", Arrays.asList("subscribe"), new EmbedBuilder()
                .setAuthor("** Tracks Command **", null, null)
                .setColor(Color.BLUE)
                .setDescription(
                        "**Description:** Tracks game from which you want to receive patches.\n" +
                        "**Usage:** Track [GAME] \n" +
                        "**Aliases:** subscribe")
                .build()
        );
    }

    @Override
    public void execute(ICommandContext context) {
        PatchTracker instance = PatchTracker.getInstance();
        IGameService gameService = instance.getGameService();
        TextChannel channel = context.getChannel();
        if (context.size() < 1) {
            channel.sendMessage(getUsage()).queue();
            return;
        }

        String gameName = String.join(" ", context.args());
        Game game = gameService.getGame(gameName);

        if (game == null) {
            game = StringHelper.sortBySimilarity(
                gameName,
                gameService.getGames(), 0.7,
                Game::getFullName
            ).stream()
            .findFirst()
            .orElse(null);
        }

        if (game == null) {
            channel.sendMessage("Game **" + gameName + "** couldn't be found").queue();
            return;
        }

        if (isTracking(game, channel)) {
            Database.INSTANCE.update(
                "DELETE FROM TRACKERS WHERE CHANNEL_ID = ? AND GAME = ?",
                channel.getIdLong(),
                game.getFullName()
            );

            channel.sendMessage("Removed tracking **" + game.getFullName() + "**").queue();
            return;
        }

        Database.INSTANCE.update(
            "INSERT INTO TRACKERS VALUES (DEFAULT, ?, ?, ?)",
            channel.getIdLong(),
            game.getFullName(),
            null
        );

        channel.sendMessage("Tracking **" + game.getFullName() + "**").queue();
    }

    private boolean isTracking(Game game, TextChannel channel) {
        return !Database.INSTANCE.query(
                "SELECT * FROM TRACKERS WHERE CHANNEL_ID = ? AND GAME = ?",
                channel.getId(),
                game.getFullName()
        ).getRows().isEmpty();
    }
}
