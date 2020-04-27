package net.neszku.patch_tracker.database;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public interface IRow {

    /**
     * Gets the mapped data for this row
     *
     * @return mapped data for this row
     */
    Map<String, Object> getData();

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    Object get(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @param clazz class to cast to
     * @param <T> cast
     * @return casted data for this column
     */
    <T> T get(String column, Class<T> clazz);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    LocalDateTime getDate(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    Timestamp getTimestamp(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    UUID getUUID(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    String getString(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    boolean getBoolean(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    byte getByte(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    short getShort(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    int getInt(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    long getLong(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    float getFloat(String column);

    /**
     * Gets the value of this row for the column
     *
     * @param column name of the column
     * @return data for this column
     */
    double getDouble(String column);
}
