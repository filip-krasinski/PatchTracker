package net.neszku.patch_tracker.patch;

import net.neszku.patch_tracker.cache.IPassiveCache;
import net.neszku.patch_tracker.page.IPageCluster;

public interface IPatchService {

    IPassiveCache<String, IPageCluster<String>> getCache();

}
