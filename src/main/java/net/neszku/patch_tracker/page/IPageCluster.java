package net.neszku.patch_tracker.page;

import java.util.List;

public interface IPageCluster<V> {

    /**
     * Returns list of {@link IPage} that belong to this cluster
     *
     * @return list of {@link IPage} that belong to this cluster
     */
    List<IPage<V>> getPages();

    /**
     * Gets the {@link IPage} of index that belongs to this cluster
     *
     * @param index index of {@link IPage}
     * @throws IllegalStateException if {@link IPage} doesn't exist
     * @return {@link IPage} of index that belongs to this cluster
     */
    IPage<V> getPage(int index);

    /**
     * Gets the next {@link IPage} that belongs to this cluster
     *
     * @throws IllegalStateException if next {@link IPage} doesn't exist
     * @return next {@link IPage} that belongs to this cluster
     */
    IPage<V> nextPage();

    /**
     * Gets the previous {@link IPage} that belongs to this cluster
     *
     * @throws IllegalStateException if previous {@link IPage} doesn't exist
     * @return previous {@link IPage} that belongs to this cluster
     */
    IPage<V> previousPage();

    /**
     * Gets the current {@link IPage} that belongs to this cluster
     *
     * @return current {@link IPage} that belongs to this cluster
     */
    IPage<V> getCurrentPage();

    /**
     * Gets the index of cursor
     *
     * @return index of cursor
     */
    int getCurrentIndex();


    /**
     * Gets the size of this cluster
     *
     * @return size of this cluster
     */
    int size();

    /**
     * Checks whether this cluster is empty
     *
     * @return the flag
     */
    boolean isEmpty();

    /**
     * Sets the cursor on provided {@link IPage}
     *
     * @param page {@link IPage} index to set cursor on
     */
    void setCurrentPage(int page);

    /**
     * Removes the {@link IPage} with provided index
     *
     * @throws IllegalStateException if page of that index doesn't exist
     * @param {@link IPage} index of {@link IPage} to remove
     */
    void removePage(int page);

    /**
     * Adds the {@link IPage} to the cluster
     *
     * @param page {@link IPage} to be added
     */
    void addPage(IPage<V> page);

    /**
     * Checks whether this cluster has next {@link IPage}
     *
     * @return the flag
     */
    boolean hasNextPage();

    /**
     * Checks whether this cluster has previous {@link IPage}
     *
     * @return the flag
     */
    boolean hasPreviousPage();
}
