package net.neszku.patch_tracker.patch.impl;

import net.neszku.patch_tracker.game.Game;
import net.neszku.patch_tracker.patch.Format;
import net.neszku.patch_tracker.patch.IPatch;
import net.neszku.patch_tracker.patch.IPatchBuilder;

import java.time.LocalDateTime;

public class PatchBuilderImpl implements
        IPatchBuilder,
        IPatchBuilder.URLBuilder,
        IPatchBuilder.TitleBuilder,
        IPatchBuilder.ContentBuilder,
        IPatchBuilder.FormatBuilder,
        IPatchBuilder.BannerBuilder,
        IPatchBuilder.PublicationDateBuilder {

    public static IPatchBuilder newBuilder() {
        return new PatchBuilderImpl();
    }

    private PatchBuilderImpl() {

    }

    protected Game game;
    protected String url;
    protected String title;
    protected String rawContent;
    protected String bannerURL;
    protected Format format;
    protected LocalDateTime publicationDate;

    @Override
    public URLBuilder game(Game game) {
        this.game = game;
        return this;
    }

    @Override
    public TitleBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public ContentBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public FormatBuilder rawContent(String rawContent) {
        this.rawContent = rawContent;
        return this;
    }

    @Override
    public BannerBuilder format(Format format) {
        this.format = format;
        return this;
    }

    @Override
    public PublicationDateBuilder bannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
        return this;
    }

    @Override
    public IPatch publicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
        return new PatchImpl(this);
    }
}
