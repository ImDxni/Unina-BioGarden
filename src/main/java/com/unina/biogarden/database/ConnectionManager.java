package com.unina.biogarden.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Manages database connections using HikariCP connection pooling.
 * This class provides a singleton instance of a {@link HikariDataSource} to ensure efficient and robust database access.
 * It initializes the connection pool when the class loads and offers methods to retrieve the data source and shut down the pool.
 */
public class ConnectionManager {
    private static final HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/BioGarden");
            config.setUsername("postgres");
            config.setPassword("admin");
            config.setDriverClassName("org.postgresql.Driver");

            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(10000);
            config.setIdleTimeout(60000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nella configurazione del DB", e);
        }
    }

    /**
     * Private constructor to prevent direct instantiation of this utility class.
     */
    private ConnectionManager() {}

    /**
     * Returns the HikariCP {@link DataSource} instance.
     * This method provides access to the connection pool for obtaining database connections.
     *
     * @return The configured {@link DataSource} for database access.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Shuts down the HikariCP connection pool.
     * This method should be called when the application is gracefully shutting down to release all database resources.
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}