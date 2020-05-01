package net.neszku.patch_tracker.task;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.neszku.patch_tracker.Emote;
import net.neszku.patch_tracker.PatchTracker;
import net.neszku.patch_tracker.database.IDatabaseResponse;
import net.neszku.patch_tracker.database.IRow;
import net.neszku.patch_tracker.database.impl.Database;
import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.game.IGameService;
import net.neszku.patch_tracker.helpers.ParserHelper;
import net.neszku.patch_tracker.page.IPageCluster;
import net.neszku.patch_tracker.patch.Format;
import net.neszku.patch_tracker.patch.IPatch;
import net.neszku.patch_tracker.patch.IPatchService;
import org.pmw.tinylog.Logger;

import java.awt.Color;

public class TrackingTask implements Runnable {

    private final PatchTracker instance;

    public TrackingTask(PatchTracker instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        Logger.info("Checking for patches...");
        IGameService  gameService  = instance.getGameService();
        IPatchService patchService = instance.getPatchService();
        for (Game game : gameService.getGames()) {
            IPatch patch = game.getLatestPatch();

            if (patchService.getCache().get(patch.getIdentifier()) != null) {
                continue; //already handled
            }

            Logger.info("Found patch for game '{}' named: '{}'",
                game.getFullName(),
                patch.getTitle()
            );

            Database.INSTANCE.update(
                "INSERT INTO PATCHES VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)",
                patch.getIdentifier(),
                game.getFullName(),
                patch.getTitle(),
                patch.getRawContent(),
                patch.getURL(),
                patch.getBannerURL(),
                patch.getFormat().name(),
                patch.getPublicationDate()
            );

            String markdown = ParserHelper.parse(
                patch.getFormat(),
                Format.MARKDOWN,
                patch.getRawContent()
            );

            IPageCluster<String> cluster = ParserHelper.chunk(markdown);

            IDatabaseResponse response = Database.INSTANCE.query(
                "SELECT * FROM TRACKERS WHERE GAME = ?",
                game.getFullName()
            );

            Logger.info("Pushing patch to {} channels", response.getRows().size());
            instance.getExecutorService().submit(() -> { //so we won't be blocking this thread as it may take some time
                for (IRow row : response.getRows()) {
                    TextChannel channel = instance.getShardManager().getTextChannelById(row.getLong("CHANNEL_ID"));

                    if (channel == null) {
                        continue;
                    }

                    channel.sendMessage(print(patch, game, cluster))
                        .queue(message -> {
                            if (cluster.size() > 1) {
                                message.addReaction(Emote.LEFT.asSnowflake()).queue();
                                message.addReaction(Emote.RIGHT.asSnowflake()).queue();
                                patchService.getCache().add(
                                    patch.getIdentifier(),
                                    cluster
                                );
                            }
                        });
                }
            });


        }
    }

    private MessageEmbed print(IPatch patch, Game game, IPageCluster<String> cluster) {
        return new EmbedBuilder()
                .setColor(Color.PINK)
                .setDescription(cluster.getCurrentPage().getData())
                .setThumbnail(game.getLogo())
                .setImage(patch.getBannerURL())
                .setTitle(patch.getTitle(), patch.getURL())
                .setFooter(String.format("Page %s / %s",
                        cluster.getCurrentIndex() + 1,
                        cluster.size()),
                        instance.getShardManager().getShards().get(0).getSelfUser().getAvatarUrl() +
                        "?" + patch.getIdentifier() //hidden identifier D:
                ).setTimestamp(patch.getPublicationDate())
                .build();
    }
}
