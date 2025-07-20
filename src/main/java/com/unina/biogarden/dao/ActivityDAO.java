package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.activity.ActivityDTO;
import com.unina.biogarden.dto.activity.HarvestingActivityDTO;
import com.unina.biogarden.dto.activity.IrrigationActivityDTO;
import com.unina.biogarden.dto.activity.SeedingActivityDTO;
import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;
import org.postgresql.util.PGobject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Data Access Object (DAO) per la gestione delle operazioni CRUD relative alle attività nel database.
 * Questa classe fornisce metodi per inserire, eliminare, recuperare e aggiornare le attività,
 * gestendo i diversi tipi di attività (Semina, Raccolta, Irrigazione) in modo polimorfico.
 * Utilizza {@link ConnectionManager} per ottenere connessioni al database.
 * @author Il Tuo Nome
 */
public class ActivityDAO {
    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Inserisce una nuova attività nel database.
     * Il tipo specifico di attività (Semina, Raccolta, Irrigazione) determina
     * quali parametri specifici dell'attività vengono impostati nella stored procedure.
     * @param object L'oggetto {@link ActivityDTO} che rappresenta l'attività da inserire.
     * @throws RuntimeException se si verifica un errore SQL durante l'inserimento.
     * @throws IllegalArgumentException se il tipo di attività non è supportato.
     */
    public void insertActivity(ActivityDTO object) {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmnt = conn.prepareCall("{? = call CreaAttivita(?,?,?,?,?,?,?,?,?,?,?)}");
            stmnt.registerOutParameter(1, Types.INTEGER);

            stmnt.setDate(2, Date.valueOf(object.getDate()));
            PGobject activityStatusObj = new PGobject();
            activityStatusObj.setType("StatoAttivita");
            activityStatusObj.setValue(object.getStatus().getStatus().toLowerCase());
            stmnt.setObject(3, activityStatusObj);
            stmnt.setInt(4, object.getFarmerID());
            stmnt.setInt(5, object.getColtureID());
            stmnt.setInt(6, object.getLotID());
            PGobject activityTypeObj = new PGobject();
            activityTypeObj.setType("TipoAttivita");
            activityTypeObj.setValue(object.getType().getDescription().toLowerCase());
            stmnt.setObject(7, activityTypeObj);

            switch (object.getType()) {
                case SEEDING -> {
                    SeedingActivityDTO seedingActivityDTO = (SeedingActivityDTO) object;
                    stmnt.setInt(8, seedingActivityDTO.getQuantity());
                    stmnt.setString(9, seedingActivityDTO.getUnit());
                    stmnt.setNull(10, Types.INTEGER);
                    stmnt.setNull(11, Types.INTEGER);
                    stmnt.setNull(12, Types.VARCHAR);
                }
                case HARVEST -> {
                    HarvestingActivityDTO harvestingActivityDTO = (HarvestingActivityDTO) object;
                    stmnt.setNull(8, Types.INTEGER);
                    stmnt.setNull(9, Types.VARCHAR);
                    stmnt.setInt(10, harvestingActivityDTO.getExpectedQuantity());
                    stmnt.setInt(11, harvestingActivityDTO.getActualQuantity());
                    stmnt.setString(12, harvestingActivityDTO.getUnit());
                }
                case IRRIGATION -> {
                    stmnt.setNull(8, Types.INTEGER);
                    stmnt.setNull(9, Types.VARCHAR);
                    stmnt.setNull(10, Types.INTEGER);
                    stmnt.setNull(11, Types.INTEGER);
                    stmnt.setNull(12, Types.VARCHAR);
                }
                default -> throw new IllegalArgumentException("Tipo attività non supportato: " + object.getType());
            }

            stmnt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante l'inserimento dell'attività: " + ex.getMessage(), ex);
        }
    }

    /**
     * Elimina un'attività dal database dato il suo ID.
     * @param activityID L'ID dell'attività da eliminare.
     * @throws RuntimeException se si verifica un errore SQL durante l'eliminazione o se nessuna attività viene trovata con l'ID specificato.
     */
    public void deleteActivity(int activityID) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("DELETE FROM Attivita WHERE id = ?");
            stmnt.setInt(1, activityID);
            int rowsAffected = stmnt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Nessuna attività trovata con ID: " + activityID);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante l'eliminazione dell'attività: " + ex.getMessage(), ex);
        }
    }

    /**
     * Recupera una collezione di attività associate a una specifica coltivazione.
     * Ogni attività viene deserializzata nel suo tipo specifico (Semina, Raccolta, Irrigazione)
     * in base al valore della colonna 'TipoAttivita'.
     * @param coltureID L'ID della coltivazione per cui recuperare le attività.
     * @return Una {@link Collection} di {@link ActivityDTO} che rappresenta le attività della coltivazione.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero dei dati.
     * @throws IllegalArgumentException se viene incontrato un tipo di attività non supportato dal database.
     */
    public Collection<ActivityDTO> fetchActivityByColture(int coltureID) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM Attivita WHERE idcoltivazione = ?");
            stmnt.setInt(1, coltureID);
            ResultSet rs = stmnt.executeQuery();
            Collection<ActivityDTO> activities = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDate date = rs.getObject("data", LocalDate.class);
                String status = rs.getString("stato");
                String type = rs.getString("TipoAttivita");
                int lotID = rs.getInt("idlotto");
                int coltureIDFromDB = rs.getInt("idcoltivazione");
                int farmerID = rs.getInt("idutente");

                ActivityType activityType = ActivityType.fromString(type);
                ActivityStatus activityStatus = ActivityStatus.fromString(status);
                ActivityDTO activity;
                switch (activityType) {
                    case SEEDING -> {
                        int quantity = rs.getInt("quantitaSemi");
                        String unit = rs.getString("UnitaMisuraSemi");
                        activity = new SeedingActivityDTO(id, date, activityStatus, quantity, unit, coltureIDFromDB, lotID, farmerID);
                    }
                    case HARVEST -> {
                        int expectedQuantity = rs.getInt("QuantitaPrevistaRaccolta");
                        int actualQuantity = rs.getInt("QuantitaEffettivaRaccolta");
                        String unit = rs.getString("UnitaMisuraRaccolta");
                        activity = new HarvestingActivityDTO(id, date, activityStatus, expectedQuantity, actualQuantity, unit, coltureIDFromDB, lotID, farmerID);
                    }
                    case IRRIGATION ->
                            activity = new IrrigationActivityDTO(id, date, activityStatus, coltureIDFromDB, lotID, farmerID);

                    default -> throw new IllegalArgumentException("Tipo attività non supportato: " + activityType);
                }

                activities.add(activity);
            }

            return activities;
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero delle attività per coltivazione: " + ex.getMessage(), ex);
        }
    }

    /**
     * Aggiorna un'attività esistente nel database.
     * I parametri aggiornati dipendono dal tipo specifico dell'attività.
     * @param object L'oggetto {@link ActivityDTO} che rappresenta l'attività da aggiornare.
     * @throws RuntimeException se si verifica un errore SQL durante l'aggiornamento.
     * @throws IllegalArgumentException se il tipo di attività non è supportato.
     */
    public void updateActivity(ActivityDTO object) {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmnt = conn.prepareCall("Call AggiornaAttivita(?,?,?,?,?,?,?,?,?,?)");

            stmnt.setInt(1, object.getId());
            stmnt.setDate(2, Date.valueOf(object.getDate()));

            PGobject activityStatusObj = new PGobject();
            activityStatusObj.setType("StatoAttivita");
            activityStatusObj.setValue(object.getStatus().getStatus().toLowerCase());
            stmnt.setObject(3, activityStatusObj);

            stmnt.setInt(4, object.getFarmerID());

            PGobject activityTypeObj = new PGobject();
            activityTypeObj.setType("TipoAttivita");
            activityTypeObj.setValue(object.getType().getDescription().toLowerCase());
            stmnt.setObject(5, activityTypeObj);

            switch (object.getType()) {
                case SEEDING -> {
                    SeedingActivityDTO seeding = (SeedingActivityDTO) object;
                    stmnt.setInt(6, seeding.getQuantity());
                    stmnt.setString(7, seeding.getUnit());

                    // Raccolta → null
                    stmnt.setNull(8, Types.INTEGER);
                    stmnt.setNull(9, Types.INTEGER);
                    stmnt.setNull(10, Types.VARCHAR);
                }
                case HARVEST -> {
                    HarvestingActivityDTO harvest = (HarvestingActivityDTO) object;
                    stmnt.setNull(6, Types.INTEGER);
                    stmnt.setNull(7, Types.VARCHAR);

                    stmnt.setInt(8, harvest.getExpectedQuantity());
                    stmnt.setInt(9, harvest.getActualQuantity());
                    stmnt.setString(10, harvest.getUnit());
                }
                case IRRIGATION -> {
                    // Tutti i campi specifici sono nulli
                    stmnt.setNull(6, Types.INTEGER);
                    stmnt.setNull(7, Types.VARCHAR);
                    stmnt.setNull(8, Types.INTEGER);
                    stmnt.setNull(9, Types.INTEGER);
                    stmnt.setNull(10, Types.VARCHAR);
                }
                default -> throw new IllegalArgumentException("Tipo attività non supportato: " + object.getType());
            }

            stmnt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'attività: " + ex.getMessage(), ex);
        }
    }

}