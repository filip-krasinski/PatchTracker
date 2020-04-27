package net.neszku.patch_tracker.database;

public interface IColumn {

    /**
     * Gets the index of this column
     *
     * @return index of this column
     */
    int getIndex();

    /**
     * Gets the name of this column
     *
     * @return name of this column
     */
    String getName();

    /**
     * Gets the type of this column
     *
     * @return type of this column
     */
    String getType();

}
