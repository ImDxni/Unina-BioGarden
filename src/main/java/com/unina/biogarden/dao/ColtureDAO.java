package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.ColtureDTO;
import com.unina.biogarden.enumerations.ColtureStatus;
import com.unina.biogarden.exceptions.ColtureAlreadyExists;
import org.postgresql.util.PGobject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Data Access Object (DAO) per la gestione delle operazioni relative alle colture nel database.
 * Questa classe fornisce metodi per aggiungere nuove colture e recuperare colture esistenti,
 * interagendo con il database tramite {@link ConnectionManager}.
 * @author Il Tuo Nome
 */
public class ColtureDAO {
    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Aggiunge una nuova coltura al database per un progetto specificato.
     * La coltura viene inizializzata con la data corrente e lo stato {@code WAITING}.
     * @param idProgetto L'ID del progetto a cui aggiungere la coltura.
     * @param idCrop L'ID del tipo di coltura (es. pomodoro, basilico).
     * @throws ColtureAlreadyExists Se esiste già una coltura dello stesso tipo in quel progetto.
     * @throws RuntimeException Se si verifica un errore SQL durante l'aggiunta della coltura.
     */
    public void addColtura(int idProgetto, int idCrop) throws ColtureAlreadyExists {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmnt = conn.prepareCall("{? = call CreaColtivazione(?, ?, ?, ?)}");

            stmnt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));

            PGobject tipoUtenteObj = new PGobject();
            tipoUtenteObj.setType("StatoColtivazione");
            tipoUtenteObj.setValue(ColtureStatus.WAITING.getStatus().toLowerCase());

            stmnt.setObject(3, tipoUtenteObj);
            stmnt.setInt(4, idCrop);
            stmnt.setInt(5, idProgetto);

            stmnt.executeUpdate();
        } catch (SQLException ex) {
            if (ex.getSQLState().equalsIgnoreCase("P0001")) {
                throw new ColtureAlreadyExists("Esiste già una coltura di quel tipo in questo progetto!");
            }
            throw new RuntimeException("Errore durante l'aggiunta della coltura: " + ex.getMessage(), ex);
        }
    }

    /**
     * Recupera una collezione di {@link ColtureDTO} associate a un progetto specifico.
     * @param projectId L'ID del progetto di cui recuperare le colture.
     * @return Una {@link Collection} di {@link ColtureDTO} che rappresenta le colture del progetto.
     * @throws RuntimeException Se si verifica un errore SQL durante il recupero delle colture.
     */
    public Collection<ColtureDTO> fetchColtures(int projectId) {
        List<ColtureDTO> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM fetchColtures(?)");
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ColtureDTO colture = new ColtureDTO(
                        rs.getInt(1),
                        rs.getDate(2).toLocalDate(),
                        ColtureStatus.fromString(rs.getString(3)),
                        projectId,
                        rs.getInt(4),
                        rs.getString(5)
                );
                results.add(colture);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero delle colture: " + ex.getMessage(), ex);
        }

        return results;
    }
}