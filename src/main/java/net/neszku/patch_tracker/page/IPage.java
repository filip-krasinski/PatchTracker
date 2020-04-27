package net.neszku.patch_tracker.page;

public interface IPage<V> {

    /**
     * Gets the cluster this page belongs to
     *
     * @return cluster this page belongs to
     */
    IPageCluster<V> belongsTo();

    /**
     * Gets the data of this page
     *
     * @return data of this page
     */
    V getData();

}
