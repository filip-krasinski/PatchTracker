package net.neszku.patch_tracker.cache;

import net.neszku.patch_tracker.cache.impl.PassiveCacheBuilderImpl;
import net.neszku.patch_tracker.cache.impl.PassiveCacheImpl;

import java.util.concurrent.TimeUnit;

public interface IPassiveCacheBuilder<K, V> {

    /**
     * Sets the time after which the values that
     * has been added are going to be considered
     * as expired. Keep in mind that values are
     * removed on access
     *
     * @param time the time
     * @param unit the unit of time
     * @return builder
     */
    PassiveCacheBuilderImpl<K, V> expireAfterWrite(long time, TimeUnit unit);

    /**
     * Sets the time after which the values that
     * are being accessed are going to be considered
     * as expired. Keep in mind that values are
     * removed on access
     *
     * @param time the time
     * @param unit the unit of time
     * @return builder
     */
    PassiveCacheBuilderImpl<K, V> expireAfterAccess(long time, TimeUnit unit);

    /**
     * Builds the {@link IPassiveCache}
     *
     * @param loader loader from which values are loaded
     * @return {@link IPassiveCache}
     */
    <K1 extends K, V1 extends V> PassiveCacheImpl<K1, V1> build(IPassiveCacheLoader<? super K1, V1> loader);

}
