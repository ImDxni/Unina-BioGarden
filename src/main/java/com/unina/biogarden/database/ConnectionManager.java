package com.unina.biogarden.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Gestisce le connessioni al database utilizzando HikariCP per il connection pooling.
 * Questa classe fornisce un'istanza singleton di {@link HikariDataSource} per garantire
 * un accesso efficiente e robusto al database. Inizializza il pool di connessioni
 * al caricamento della classe e offre metodi per recuperare il data source e chiudere il pool.
 * @author Il Tuo Nome
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

            config.setMaximumPoolSize(10); // Dimensione massima del pool di connessioni
            config.setConnectionTimeout(10000); // Timeout per ottenere una connessione dal pool (10 secondi)
            config.setIdleTimeout(60000); // Tempo massimo che una connessione può rimanere inattiva nel pool (60 secondi)
            config.setMaxLifetime(1800000); // Tempo massimo di vita di una connessione nel pool (30 minuti)

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nella configurazione del DB", e);
        }
    }

    /**
     * Costruttore privato per prevenire l'istanziazione diretta di questa classe utility.
     */
    private ConnectionManager() {
    }

    /**
     * Restituisce l'istanza di {@link DataSource} di HikariCP.
     * Questo metodo fornisce l'accesso al pool di connessioni per ottenere connessioni al database.
     *
     * @return Il {@link DataSource} configurato per l'accesso al database.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Chiude il pool di connessioni di HikariCP.
     * Questo metodo dovrebbe essere chiamato quando l'applicazione si sta chiudendo in modo controllato
     * per rilasciare tutte le risorse del database.
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}