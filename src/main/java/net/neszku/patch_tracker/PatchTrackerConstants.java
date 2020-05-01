package net.neszku.patch_tracker;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class PatchTrackerConstants {

    public static final String COMMANDS_PACKAGE = "net.neszku.patch_tracker.command.impl";
    public static final String GAMES_PACKAGE    = "net.neszku.patch_tracker.game.impl";

    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .followRedirects(true)
            .retryOnConnectionFailure(false)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(20, 2, TimeUnit.MINUTES))
            .build();

    public static String CREATE_TABLE_PATCHES =
            "CREATE TABLE IF NOT EXISTS PATCHES (" +
            "ID INT NOT NULL AUTO_INCREMENT, " +
            "IDENTIFIER TEXT NOT NULL, " +
            "GAME TEXT, " +
            "TITLE TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci, " +
            "RAW_CONTENT MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci, " +
            "URL TEXT, " +
            "URL_BANNER TEXT, " +
            "FORMAT TEXT, " +
            "PUBLICATION_DATE TIMESTAMP NOT NULL, " +
            "PRIMARY KEY (ID)) CHARACTER SET utf8mb4";

    public static String CREATE_TABLE_TRACKERS =
            "CREATE TABLE IF NOT EXISTS TRACKERS (" +
            "ID INT NOT NULL AUTO_INCREMENT, " +
            "CHANNEL_ID BIGINT, " +
            "GAME TEXT, " +
            "SUBSCRIBED_AT TIMESTAMP NOT NULL, " +
            "PRIMARY KEY (ID))";
}
