package net.neszku.patch_tracker.game.impl;

import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.patch.IPatch;

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
        return null;
    }

    @Override
    public List<IPatch> getLatestPatches(int howMany) {
        return null;
    }
}
