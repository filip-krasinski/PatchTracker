package net.neszku.patch_tracker.cache;

import javax.annotation.Nullable;

public interface IPassiveCacheLoader<K, V> {

    /**
     * Loads the data
     *
     * @param key to search the data by
     * @return the data
     */
    @Nullable V load(K key);

}
