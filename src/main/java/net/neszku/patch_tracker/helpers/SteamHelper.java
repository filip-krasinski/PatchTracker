package net.neszku.patch_tracker.helpers;

import net.neszku.patch_tracker.PatchTrackerConstants;
import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.game.impl.SteamGame;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SteamHelper {

    private static final String URL_APPS = "http://api.steampowered.com/ISteamApps/GetAppList/v0002/";
    private static final String URL_LOGO = "https://steamcdn-a.akamaihd.net/steam/apps/${APPID}/logo.png";
    private static final Map<Integer, String> APPS = new HashMap<>();

    static {
        Request request = new Request.Builder()
                .url(URL_APPS)
                .build();

        try (Response response = PatchTrackerConstants.HTTP_CLIENT
                .newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Body is null");
            }

            JSONArray appsArray = new JSONObject(body.string())
                    .getJSONObject("applist")
                    .getJSONArray("apps");

            for (int i = 0; i < appsArray.length(); i++) {
                JSONObject app = appsArray.getJSONObject(i);
                APPS.put(app.getInt("appid"), app.getString("name"));
            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
            games.add(new SteamGame(APPS.get(appid), custom_logo, appid));
        }

        return games;
    }

}
