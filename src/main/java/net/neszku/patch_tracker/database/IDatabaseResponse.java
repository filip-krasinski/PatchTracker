package net.neszku.patch_tracker.database;

import java.util.List;

public interface IDatabaseResponse {

    /**
     * Gets the columns for this response
     *
     * @return columns for this response
     */
    List<IColumn> getColumns();

    /**
     * Gets the rows for this response
     *
     * @return rows for this response
     */
    List<IRow> getRows();

}
