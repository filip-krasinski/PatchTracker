package net.neszku.patch_tracker;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.neszku.patch_tracker.command.Command;
import net.neszku.patch_tracker.command.ICommandService;
import net.neszku.patch_tracker.command.impl.CommandServiceImpl;
import net.neszku.patch_tracker.database.impl.Database;
import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.game.IGameService;
import net.neszku.patch_tracker.game.impl.GameServiceImpl;
import net.neszku.patch_tracker.helpers.IOHelper;
import net.neszku.patch_tracker.helpers.Reflections;
import net.neszku.patch_tracker.helpers.SteamHelper;
import net.neszku.patch_tracker.listeners.CommandListener;
import net.neszku.patch_tracker.listeners.ReactionListener;
import net.neszku.patch_tracker.patch.IPatchService;
import net.neszku.patch_tracker.patch.impl.PatchServiceImpl;
import net.neszku.patch_tracker.task.TrackingTask;
import org.slf4j.Logger;
import org.slf4j.impl.TinylogLogger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PatchTracker {

    public static final Logger LOGGER = new TinylogLogger("LOGGER");
    private static PatchTracker instance;

    private final ShardManager shardManager;
    private final IGameService gameService;
    private final IPatchService patchService;
    private final ICommandService commandService;
    private final ExecutorService executorService;


    public PatchTracker() throws LoginException {
        gameService     = new GameServiceImpl();
        patchService    = new PatchServiceImpl();
        commandService  = new CommandServiceImpl();
        executorService = Executors.newCachedThreadPool();

        LOGGER.info("Saving resources...");
        IOHelper.saveResource("config.json",     new File(IOHelper.getJarDirectory(), "config.json"));
        IOHelper.saveResource("steam_apps.json", new File(IOHelper.getJarDirectory(), "steam_apps.json"));

        LOGGER.info("Making tables....");
        Database.INSTANCE.update(PatchTrackerConstants.CREATE_TABLE_PATCHES);
        Database.INSTANCE.update(PatchTrackerConstants.CREATE_TABLE_TRACKERS);

        LOGGER.info("Loading commands...");
        Reflections.getAndInstantiate(PatchTrackerConstants.COMMANDS_PACKAGE, Command.class)
                .forEach(commandService::registerCommand);

        LOGGER.info("Loading games...");
        Reflections.getAndInstantiate(PatchTrackerConstants.GAMES_PACKAGE, Game.class)
                .forEach(gameService::registerGame);
        SteamHelper.loadSteamGames()
                .forEach(gameService::registerGame);

        LOGGER.info("Spawning shards...");
        shardManager = DefaultShardManagerBuilder.create(
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_EMOJIS
                    ).disableCache(
                        CacheFlag.ACTIVITY,
                        CacheFlag.VOICE_STATE,
                        CacheFlag.CLIENT_STATUS
                    ).setMemberCachePolicy(MemberCachePolicy.NONE)
                    .setActivity(Activity.streaming("obiadeczek", "https://www.youtube.com/watch?v=0CovDKJIMF4"))
                    .setToken(Config.BOT_TOKEN)
                    .setShardsTotal(-1)
                    .setAutoReconnect(true)
                    .addEventListeners(
                       new ReactionListener(this),
                       new CommandListener(this)
                    )
                    .build();

        LOGGER.info("Spawning tasks...");
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(new TrackingTask(this), 5, 500, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws LoginException {
        instance = new PatchTracker();
    }

    public static PatchTracker getInstance() {
        return instance;
    }

    public IGameService getGameService() {
        return gameService;
    }

    public IPatchService getPatchService() {
        return patchService;
    }

    public ICommandService getCommandService() {
        return commandService;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
