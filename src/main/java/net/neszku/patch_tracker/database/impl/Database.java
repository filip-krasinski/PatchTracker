package net.neszku.patch_tracker.database.impl;

import com.zaxxer.hikari.HikariDataSource;
import net.neszku.patch_tracker.Config;
import net.neszku.patch_tracker.database.IColumn;
import net.neszku.patch_tracker.database.IDatabaseResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public enum Database {
    INSTANCE;

    private final HikariDataSource dataSource;

    Database() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://" + Config.MYSQL_HOST + ":" + Config.MYSQL_PORT + "/" + Config.MYSQL_BASE);
        dataSource.setUsername(Config.MYSQL_USER);
        dataSource.setPassword(Config.MYSQL_PASS);
        dataSource.setAutoCommit(true);
        dataSource.setMaxLifetime(480000);
        dataSource.setMaximumPoolSize(10);
        dataSource.setConnectionTimeout(5000);
        dataSource.setPoolName("HikariMySQL");

        dataSource.addDataSourceProperty("characterEncoding","utf8");
        dataSource.addDataSourceProperty("useUnicode","true");
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("useServerPrepStmts", true);
        dataSource.addDataSourceProperty("useLocalSessionState", true);
        dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
        dataSource.addDataSourceProperty("cacheResultSetMetadata", true);
        dataSource.addDataSourceProperty("cacheServerConfiguration", true);
        dataSource.addDataSourceProperty("elideSetAutoCommits", true);
        dataSource.addDataSourceProperty("maintainTimeStats", false);
        dataSource.addDataSourceProperty("autoClosePStmtStreams", true);
        dataSource.addDataSourceProperty("useSSL", Config.USE_SSL);

        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public void update(String sql, Object... replacements) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement  = connection.prepareStatement(sql)) {

            for (int i = 0; i < replacements.length; ++i) {
                statement.setObject(i + 1, replacements[i]);
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public IDatabaseResponse query(String sql, Object... replacements) {
        DatabaseResponseImpl response   = new DatabaseResponseImpl();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement  = connection.prepareStatement(sql)) {

            for (int i = 0; i < replacements.length; ++i) {
                statement.setObject(i + 1, replacements[i]);
            }

            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData  = resultSet.getMetaData();

            for (int i = 1; i < metaData.getColumnCount() + 1; ++i) {
                response.addColumn(new ColumnImpl(i,
                    metaData.getColumnName(i),
                    metaData.getColumnTypeName(i)
                ));
            }

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (IColumn column : response.getColumns()) {
                    row.put(column.getName(), resultSet.getObject(column.getName()));
                }
                response.addRow(new RowImpl(row));
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }

}