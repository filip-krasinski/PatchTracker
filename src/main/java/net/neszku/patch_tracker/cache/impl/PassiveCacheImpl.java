package net.neszku.patch_tracker.cache.impl;

import net.neszku.patch_tracker.cache.IPassiveCache;
import net.neszku.patch_tracker.cache.IPassiveCacheLoader;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PassiveCacheImpl<K, V> implements IPassiveCache<K, V> {

    private final Long writeExpiration;
    private final Long accessExpiration;
    private final IPassiveCacheLoader<? super K, V> loader;
    private final Map<K, PassiveCacheEntry<V>> map = new ConcurrentHashMap<>();

    public PassiveCacheImpl(PassiveCacheBuilderImpl<? super K, ? super V> builder, IPassiveCacheLoader<? super K, V> loader) {
        this.loader = loader;
        this.writeExpiration = builder.writeExpiration;
        this.accessExpiration = builder.accessExpiration;
    }

    @Override
    @Nullable
    public V add(K k, V v) {
        onAccess(k);
        PassiveCacheEntry<V> old = map.put(k, new PassiveCacheEntry<>(v, System.currentTimeMillis() + writeExpiration));
        return old == null ? null : old.value;
    }

    @Override
    public boolean contains(K k) {
        onAccess(k);
        return map.containsKey(k);
    }

    @Nullable
    @Override
    public V remove(K k) {
        onAccess(k);
        PassiveCacheEntry<V> removed = map.remove(k);
        return removed == null ? null : removed.value;
    }

    @Nullable
    @Override
    public V get(K k) {
        onAccess(k);
        PassiveCacheEntry<V> val = map.get(k);
        if (val == null) {
            V loaded = loader.load(k);
            if (loaded != null) {
                map.put(k, map.put(k, new PassiveCacheEntry<>(loaded, System.currentTimeMillis() + accessExpiration)));
                return loaded;
            }
        }
        return val == null ? null : val.value;
    }

    private void onAccess(K k) {
        long now = System.currentTimeMillis();
        map.entrySet().removeIf(next -> now >= next.getValue().validTo);
        if (accessExpiration != null) {
            PassiveCacheEntry<V> pair = map.get(k);
            if (pair == null) {
                return;
            }
            pair.validTo = System.currentTimeMillis() + accessExpiration;
        }
    }

    private static class PassiveCacheEntry<V> {

        private final V value;
        private Long validTo;

        private PassiveCacheEntry(V value, Long validTo) {
            this.value = value;
            this.validTo = validTo;
        }
    }
}