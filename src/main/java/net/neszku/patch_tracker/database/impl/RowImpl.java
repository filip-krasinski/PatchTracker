package net.neszku.patch_tracker.database.impl;

import net.neszku.patch_tracker.database.IRow;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class RowImpl implements IRow {

    private final Map<String, Object> data;

    public RowImpl(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public Object get(String column) {
        return data.get(column);
    }

    @Override
    public <T> T get(String column, Class<T> clazz) {
        return clazz.cast(data.get(column));
    }

    @Override
    public LocalDateTime getDate(String column) {
        return getTimestamp(column).toLocalDateTime();
    }

    @Override
    public Timestamp getTimestamp(String column) {
        return ((Timestamp) get(column));
    }

    @Override
    public UUID getUUID(String column) {
        return UUID.fromString(getString(column));
    }

    @Override
    public String getString(String column) {
        return (String) get(column);
    }

    @Override
    public boolean getBoolean(String column) {
        return (boolean) get(column);
    }

    @Override
    public byte getByte(String column) {
        return (byte) get(column);
    }

    @Override
    public short getShort(String column) {
        return (short) get(column);
    }

    @Override
    public int getInt(String column) {
        return (int) get(column);
    }

    @Override
    public long getLong(String column) {
        return (long) get(column);
    }

    @Override
    public float getFloat(String column) {
        return (float) get(column);
    }

    @Override
    public double getDouble(String column) {
        return (double) get(column);
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }
}