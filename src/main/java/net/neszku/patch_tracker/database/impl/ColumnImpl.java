package net.neszku.patch_tracker.database.impl;

import net.neszku.patch_tracker.database.IColumn;

public class ColumnImpl implements IColumn {

    private final int index;
    private final String name;
    private final String type;

    public ColumnImpl(int index, String name, String type) {
        this.index = index;
        this.name = name;
        this.type = type;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

}