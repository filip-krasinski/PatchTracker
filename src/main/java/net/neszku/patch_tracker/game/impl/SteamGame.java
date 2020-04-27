package net.neszku.patch_tracker.game.impl;

import net.neszku.patch_tracker.PatchTrackerConstants;
import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.helpers.SteamHelper;
import net.neszku.patch_tracker.patch.Format;
import net.neszku.patch_tracker.patch.IPatch;
import net.neszku.patch_tracker.patch.impl.PatchBuilderImpl;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SteamGame extends Game {

    private final int appId;

    public SteamGame(String fullName, String logo, int appId) {
        super(fullName, logo);
        this.appId = appId;
    }

    public int getAppId() {
        return appId;
    }

    @Override
    public IPatch getLatestPatch() {
        return getLatestPatches(1).get(0);
    }

    @Override
    public List<IPatch> getLatestPatches(int howMany) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.steampowered.com")
                .addEncodedPathSegments("ISteamNews/GetNewsForApp/v2/")
                .addQueryParameter("appid", String.valueOf(appId))
                .addQueryParameter("feeds", "steam_community_announcements")
                .addQueryParameter("format", "json")
                .addQueryParameter("count", String.valueOf(howMany))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = PatchTrackerConstants.HTTP_CLIENT
                .newCall(request).execute()) {

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Body is null");
            }

            JSONObject responseObject = new JSONObject(body.string());
            JSONArray patchesArray = responseObject.getJSONObject("appnews")
                    .getJSONArray("newsitems");

            List<IPatch> patches = new ArrayList<>();
            for (int i = 0; i < patchesArray.length(); i++) {
                JSONObject patchObject = patchesArray.getJSONObject(i);
                String title = patchObject.getString("title");
                String directUrl = patchObject.getString("url");
                String content = patchObject.getString("contents");
                LocalDateTime date = LocalDateTime.ofEpochSecond(patchObject.getLong("date"), 0, ZoneOffset.UTC);
                String banner = SteamHelper.extractBanner(content);

                IPatch patch = PatchBuilderImpl.newBuilder()
                        .game(this)
                        .url(directUrl)
                        .title(title)
                        .rawContent(content)
                        .format(Format.BBCODE)
                        .bannerURL(banner)
                        .publicationDate(date);

                patches.add(patch);
            }

            return patches;
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        return Collections.emptyList();
    }
}
