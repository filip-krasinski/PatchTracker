package net.neszku.patch_tracker.cache;

import javax.annotation.Nullable;

public interface IPassiveCache<K, V> {

    /**
     * Adds data to the cache
     *
     * @param k key
     * @param v value
     * @return the previous value associated with the key or null
     */
    @Nullable V add(K k, V v);

    /**
     * Checks whether cache contains
     * value associated with the key
     *
     * @param k key to check
     * @return the flag
     */
    boolean contains(K k);

    /**
     * Removes the key from the cache
     *
     * @param k key to remove
     * @return the previous value associated with the key or null
     */
    @Nullable V remove(K k);

    /**
     * Gets the value associated with the key
     *
     * @param k key to find
     * @return value associated with the key or null
     */
    @Nullable V get(K k);

}
