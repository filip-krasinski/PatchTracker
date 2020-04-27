package net.neszku.patch_tracker.game;

import net.neszku.patch_tracker.patch.IPatch;

import java.util.List;

public abstract class Game {

    private final String fullName;
    private final String logo;

    public Game(String fullName, String logo) {
        this.fullName = fullName;
        this.logo = logo;
    }

    /**
     * Gets the full name of the game
     *
     * @return full name of the game
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the url to the logo
     *
     * @return file with the icon image
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Returns the latest patch
     *
     * @return the latest patches
     */
    public abstract IPatch getLatestPatch();

    /**
     * Returns the set of the latest patches
     *
     * @param howMany - how many patches we'll try to get
     * @return set of the latest patches
     */
    public abstract List<IPatch> getLatestPatches(int howMany);

}