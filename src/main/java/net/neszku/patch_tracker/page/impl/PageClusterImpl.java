package net.neszku.patch_tracker.page.impl;

import net.neszku.patch_tracker.page.IPage;
import net.neszku.patch_tracker.page.IPageCluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageClusterImpl<V> implements IPageCluster<V> {

    private final List<IPage<V>> cluster;
    private int cursor;

    public PageClusterImpl() {
        this.cluster = Collections.synchronizedList(new ArrayList<>());
        this.cursor = 0;
    }

    @Override
    public List<IPage<V>> getPages() {
        return cluster;
    }

    @Override
    public IPage<V> getPage(int index) {
        if (index >= size()) {
            throw new IllegalStateException("Tried to access page that doesn't exist");
        }
        return cluster.get(index);
    }

    @Override
    public IPage<V> nextPage() {
        if (!hasPreviousPage()) {
            throw new IllegalStateException("Tried to access page that doesn't exist");
        }
        return cluster.get(++cursor);
    }

    @Override
    public IPage<V> previousPage() {
        if (!hasPreviousPage()) {
            throw new IllegalStateException("Tried to access page that doesn't exist");
        }
        return cluster.get(--cursor);
    }

    @Override
    public IPage<V> getCurrentPage() {
        return cluster.get(cursor);
    }

    @Override
    public int getCurrentIndex() {
        return cursor;
    }

    @Override
    public boolean isEmpty() {
        return cluster.isEmpty();
    }

    @Override
    public int size() {
        return cluster.size();
    }

    @Override
    public void setCurrentPage(int page) {
        if (page >= size()) {
            throw new IllegalStateException("Tried to set cursor on page that doesn't exist");
        }
        cursor = page;
    }

    @Override
    public void removePage(int page) {
        if (page >= size()) {
            throw new IllegalStateException("Tried to remove page that doesn't exist");
        }
        cluster.remove(page);
    }

    @Override
    public void addPage(IPage<V> page) {
        cluster.add(page);
    }

    @Override
    public boolean hasNextPage() {
        return cursor < cluster.size() - 1;
    }

    @Override
    public boolean hasPreviousPage() {
        return cursor > 0;
    }
}
