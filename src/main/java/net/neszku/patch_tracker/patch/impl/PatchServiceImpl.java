package net.neszku.patch_tracker.patch.impl;

import net.neszku.patch_tracker.PatchTracker;
import net.neszku.patch_tracker.cache.IPassiveCache;
import net.neszku.patch_tracker.cache.impl.PassiveCacheBuilderImpl;
import net.neszku.patch_tracker.database.IDatabaseResponse;
import net.neszku.patch_tracker.database.IRow;
import net.neszku.patch_tracker.database.impl.Database;
import net.neszku.patch_tracker.game.IGameService;
import net.neszku.patch_tracker.helpers.ParserHelper;
import net.neszku.patch_tracker.page.IPageCluster;
import net.neszku.patch_tracker.patch.Format;
import net.neszku.patch_tracker.patch.IPatch;
import net.neszku.patch_tracker.patch.IPatchService;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PatchServiceImpl implements IPatchService {

    public final IPassiveCache<String, IPageCluster<String>> patchesCache = PassiveCacheBuilderImpl.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(key -> {
                IDatabaseResponse response = Database.INSTANCE
                        .query("SELECT * FROM PATCHES WHERE IDENTIFIER = ?", key);

                List<IRow> rows = response.getRows();
                if (rows.isEmpty()) {
                    return null;
                }

                IRow row = rows.get(0);

                PatchTracker instance = PatchTracker.getInstance();
                IGameService gameService = instance.getGameService();
                IPatch patch = PatchBuilderImpl.newBuilder()
                        .game(gameService.getGame(row.getString("GAME")))
                        .identifier(row.getString("IDENTIFIER"))
                        .url(row.getString("URL"))
                        .title(row.getString("TITLE"))
                        .rawContent(row.getString("RAW_CONTENT"))
                        .format(Format.valueOf(row.getString("FORMAT")))
                        .bannerURL(row.getString("URL_BANNER"))
                        .publicationDate(row.getTimestamp("PUBLICATION_DATE").toLocalDateTime());

                String markdown = ParserHelper.parse(
                        patch.getFormat(),
                        Format.MARKDOWN,
                        patch.getRawContent()
                );

                return ParserHelper.chunk(markdown);
            });

    @Override
    public IPassiveCache<String, IPageCluster<String>> getCache() {
        return patchesCache;
    }
}
