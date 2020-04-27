package net.neszku.patch_tracker.database.impl;

import net.neszku.patch_tracker.database.IColumn;
import net.neszku.patch_tracker.database.IDatabaseResponse;
import net.neszku.patch_tracker.database.IRow;
import net.neszku.patch_tracker.database.impl.ColumnImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseResponseImpl implements IDatabaseResponse {

    public static final DatabaseResponseImpl EMPTY_RESPONSE = new DatabaseResponseImpl();

    private final List<IColumn> columns;
    private final List<IRow> rows;

    public DatabaseResponseImpl() {
        this.columns = new ArrayList<>();
        this.rows    = new ArrayList<>();
    }

    @Override
    public List<IColumn> getColumns() {
        return columns;
    }

    @Override
    public List<IRow> getRows() {
        return rows;
    }


    public void addColumn(IColumn column) {
        columns.add(column);
    }

    public void addRow(IRow row) {
        rows.add(row);
    }

}
