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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        List<IPatch> latestPatches = getLatestPatches(1);
        return latestPatches.isEmpty() ? null : latestPatches.get(0);
    }

    private static final Pattern PATTERN_PATCHES = Pattern.compile("data-initialEvents.*=.*\"(.*?)\".*data-appcapsulestore");
    private static final String URL_DIRECT_PATCH = "https://store.steampowered.com/newshub/app/582010/view/${GID}";

    @Override
    public List<IPatch> getLatestPatches(int howMany) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("store.steampowered.com")
                .addEncodedPathSegments("newshub/app/")
                .addPathSegment(String.valueOf(appId))
                .build();

        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0")
                .url(url)
                .get()
                .build();

        try (Response response = PatchTrackerConstants.HTTP_CLIENT
                .newCall(request).execute()) {

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Body is null");
            }

            String html = body.string();
            Matcher matcher = PATTERN_PATCHES.matcher(html);
            if (!matcher.find()) {
                return Collections.emptyList();
            }

            String jsonString = matcher.group(1)
                    .replace("&quot", "\"")
                    .replace("\";", "\"");

            JSONArray events = new JSONObject(jsonString)
                    .getJSONArray("events");

            return StreamSupport.stream(events.spliterator(), false)
                    .map(JSONObject.class::cast)
                    .filter(obj -> isPatch(obj.getInt("event_type")))
                    .limit(howMany) //news are ordered so we dont need to sort them
                    .map(obj -> {
                        JSONObject ann = obj.getJSONObject("announcement_body");
                        return PatchBuilderImpl.newBuilder()
                                .game(this)
                                .identifier(obj.getString("gid"))
                                .url(URL_DIRECT_PATCH.replace("${GID}", obj.getString("gid")))
                                .title(obj.getString("event_name"))
                                .rawContent(ann.getString("body"))
                                .format(Format.BBCODE)
                                .bannerURL(SteamHelper.extractBanner(ann.getString("body")))
                                .publicationDate(LocalDateTime.ofEpochSecond(ann.getLong("posttime"), 0, ZoneOffset.UTC));
                    })
                    .collect(Collectors.toList());

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private boolean isPatch(int event) {
        return event == 12 || event == 13 || event == 14;
    }
}
