package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.LotDTO;
import com.unina.biogarden.session.Session;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Access Object (DAO) per la gestione delle operazioni CRUD relative ai lotti nel database.
 * Questa classe fornisce metodi per creare nuovi lotti, recuperare tutti i lotti
 * associati all'utente corrente e recuperare un lotto specifico tramite il suo ID.
 * Utilizza {@link ConnectionManager} per ottenere connessioni al database e
 * {@link Session} per accedere all'ID dell'utente proprietario.
 * @author Il Tuo Nome
 */
public class LotDAO {

    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Crea un nuovo lotto nel database.
     * Il lotto viene associato all'ID dell'utente attualmente loggato nella sessione.
     * @param name Il nome del lotto.
     * @param area L'area del lotto in metri quadrati.
     * @return Un oggetto {@link LotDTO} che rappresenta il lotto appena creato,
     * con l'ID assegnato dal database, o {@code null} in caso di errore.
     * @throws RuntimeException se si verifica un errore SQL durante la creazione del lotto.
     */
    public LotDTO createPlot(String name, int area) {
        int ownerID = Session.getUtente().id();
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmnt = conn.prepareCall("{ ? = call CreaLotto(?, ?,?) }");
            stmnt.registerOutParameter(1, java.sql.Types.INTEGER);

            stmnt.setString(2, name);
            stmnt.setInt(3, area);
            stmnt.setInt(4, ownerID);

            stmnt.executeUpdate();

            int plotID = stmnt.getInt(1);
            return new LotDTO(plotID, name, area);
        } catch (SQLException ex) {
            System.err.println("Errore durante la creazione del lotto: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Errore durante la creazione del lotto.", ex);
        }
    }

    /**
     * Recupera tutti i lotti posseduti dall'utente attualmente loggato.
     * @return Una {@link Collection} di oggetti {@link LotDTO} che rappresentano tutti i lotti
     * dell'utente corrente. Restituisce una collezione vuota se non ci sono lotti
     * o in caso di errore.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero dei lotti.
     */
    public Collection<LotDTO> getAllLots() {
        Set<LotDTO> lots = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM lotto WHERE idutente = ?");

            stmnt.setInt(1, Session.getUtente().id());
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                lots.add(new LotDTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("area")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Errore durante il recupero di tutti i lotti: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Errore durante il recupero di tutti i lotti.", ex);
        }

        return lots;
    }

    /**
     * Recupera un lotto specifico dal database tramite il suo ID.
     * @param id L'ID del lotto da recuperare.
     * @return Un oggetto {@link LotDTO} che rappresenta il lotto trovato,
     * o {@code null} se nessun lotto con l'ID specificato viene trovato
     * o in caso di errore.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero del lotto.
     */
    public LotDTO getLotById(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM lotto WHERE id = ?");
            stmnt.setInt(1, id);
            ResultSet rs = stmnt.executeQuery();

            if (rs.next()) {
                return new LotDTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("area")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Errore durante il recupero del lotto per ID: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Errore durante il recupero del lotto per ID.", ex);
        }
        return null;
    }
}