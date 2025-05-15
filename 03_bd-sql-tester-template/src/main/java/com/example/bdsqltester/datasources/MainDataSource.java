package com.example.bdsqltester.datasources;

import com.zaxxer.hikari.*;

import java.sql.*;

public class MainDataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/sql-tester");
        config.setUsername("postgres");
        // TODO: Don't forget to change password!
        config.setPassword("janganh3ck!");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private MainDataSource() {
    }
}
