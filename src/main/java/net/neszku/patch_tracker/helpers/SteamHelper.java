package net.neszku.patch_tracker.helpers;

import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.game.impl.SteamGame;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamHelper {

    private static final String CLANS_URL = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/clans";
    private static final Pattern BANNER_PATTERN = Pattern.compile("\\[img]\\{STEAM_CLAN_IMAGE}(.*?)\\[/img]");
    private static final String URL_LOGO = "https://steamcdn-a.akamaihd.net/steam/apps/${APPID}/logo.png";

    public static Set<Game> loadSteamGames() {
        Set<Game> games = new HashSet<>();
        File config = new File(IOHelper.getJarDirectory(), "steam_apps.json");
        JSONArray array = new JSONArray(new String(IOHelper.readBytes(config)));
        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            int appid = object.getInt("appid");
            String custom_logo = URL_LOGO.replace("${APPID}", String.valueOf(appid));
            if (object.has("custom_logo")) {
                custom_logo = object.getString("custom_logo");
            }
            games.add(new SteamGame(object.getString("name"), custom_logo, appid));
        }

        return games;
    }

    public static String extractBanner(String rawContent) {
        Matcher matcher = BANNER_PATTERN.matcher(rawContent);
        if (matcher.find()) {
            return CLANS_URL + matcher.group(1);
        }
        return null;
    }

}
