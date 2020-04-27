package net.neszku.patch_tracker.cache.impl;

import net.neszku.patch_tracker.cache.IPassiveCacheBuilder;
import net.neszku.patch_tracker.cache.IPassiveCacheLoader;

import java.util.concurrent.TimeUnit;

public class PassiveCacheBuilderImpl<K, V> implements IPassiveCacheBuilder<K, V> {

    protected Long writeExpiration = null;
    protected Long accessExpiration = null;

    public PassiveCacheBuilderImpl<K, V> expireAfterWrite(long val, TimeUnit unit) {
        this.writeExpiration = unit.toMillis(val);
        return this;
    }

    public PassiveCacheBuilderImpl<K, V> expireAfterAccess(long val, TimeUnit unit) {
        this.accessExpiration = unit.toMillis(val);
        return this;
    }

    public <K1 extends K, V1 extends V> PassiveCacheImpl<K1, V1> build(IPassiveCacheLoader<? super K1, V1> loader) {
        return new PassiveCacheImpl<>(this, loader);
    }

    public static <K, V> PassiveCacheBuilderImpl<K, V> newBuilder() {
        return new PassiveCacheBuilderImpl<>();
    }

}
