package net.neszku.patch_tracker.patch;

import net.neszku.patch_tracker.game.Game;

import java.time.LocalDateTime;
import java.util.Objects;

public interface IPatch {

    /**
     * Gets the {@link Game} patch belongs to
     *
     * @return {@link Game} patch belongs to
     */
    Game getGame();

    /**
     * Gets the url to this patch
     *
     * @return url to this patch
     */
    String getURL();

    /**
     * Gets the title of this patch
     *
     * @return title of this patch
     */
    String getTitle();

    /**
     * Gets the raw content of this patch
     *
     * @return raw content of this patch
     */
    String getRawContent();

    /**
     * Gets the format of the raw content
     *
     * @return format of the raw content
     */
    Format getFormat();

    /**
     * Gets the banner url for this patch
     *
     * @return banner url for this patch
     */
    String getBannerURL();

    /**
     * Gets the {@link LocalDateTime} publication date
     *
     * @return {@link LocalDateTime} publication date
     */
    LocalDateTime getPublicationDate();


    default int toHashCode() {
        return Objects.hash(getTitle(), getRawContent());
    }

}
