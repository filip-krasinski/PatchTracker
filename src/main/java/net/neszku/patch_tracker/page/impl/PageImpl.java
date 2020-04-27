package net.neszku.patch_tracker.page.impl;

import net.neszku.patch_tracker.page.IPage;
import net.neszku.patch_tracker.page.IPageCluster;

public class PageImpl<V> implements IPage<V> {

    private final IPageCluster<V> cluster;
    private final V data;

    public PageImpl(IPageCluster<V> cluster, V data) {
        this.cluster = cluster;
        this.data = data;
    }

    @Override
    public IPageCluster<V> belongsTo() {
        return cluster;
    }

    @Override
    public V getData() {
        return data;
    }
}
