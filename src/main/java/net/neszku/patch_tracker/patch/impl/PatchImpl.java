package net.neszku.patch_tracker.patch.impl;

import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.patch.Format;
import net.neszku.patch_tracker.patch.IPatch;

import java.time.LocalDateTime;

public class PatchImpl implements IPatch {

    private final Game game;
    private final String identifier;
    private final String url;
    private final String title;
    private final String bannerURL;
    private final String rawContent;
    private final Format format;
    private final LocalDateTime publicationDate;

    public PatchImpl(PatchBuilderImpl builder) {
        this.game = builder.game;
        this.identifier = builder.identifier;
        this.url = builder.url;
        this.title = builder.title;
        this.bannerURL = builder.bannerURL;
        this.rawContent = builder.rawContent;
        this.format = builder.format;
        this.publicationDate = builder.publicationDate;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getRawContent() {
        return rawContent;
    }

    @Override
    public Format getFormat() {
        return format;
    }

    @Override
    public String getBannerURL() {
        return bannerURL;
    }

    @Override
    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }


    @Override
    public String toString() {
        return "PatchImpl{" +
                "game=" + game +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", bannerURL='" + bannerURL + '\'' +
                ", publicationDate=" + publicationDate +
                '}';
    }

}
