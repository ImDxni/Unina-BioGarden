package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.ProjectDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Access Object (DAO) per la gestione delle operazioni CRUD relative ai progetti nel database.
 * Questa classe fornisce metodi per creare, recuperare progetti per lotto, recuperare tutti i progetti,
 * e recuperare progetti per ID o per ID di coltivazione associata.
 * Utilizza {@link ConnectionManager} per ottenere connessioni al database.
 * @author Il Tuo Nome
 */
public class ProjectDAO {

    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Crea un nuovo progetto nel database.
     * @param nome Il nome del progetto.
     * @param dataInizio La data di inizio del progetto.
     * @param dataFine La data di fine prevista per il progetto.
     * @param idLotto L'ID del lotto a cui è associato il progetto.
     * @return Un oggetto {@link ProjectDTO} che rappresenta il progetto appena creato,
     * con l'ID assegnato dal database.
     * @throws IllegalStateException se la data di inizio è successiva alla data di fine (errore gestito dalla stored procedure).
     * @throws RuntimeException se si verifica un errore SQL durante la creazione del progetto.
     */
    public ProjectDTO creaProgetto(String nome, LocalDate dataInizio, LocalDate dataFine, int idLotto) {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmnt = conn.prepareCall("{ ? = call CreaProgetto(?, ?, ?, ?) }");
            stmnt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmnt.setString(2, nome);
            stmnt.setDate(3, Date.valueOf(dataInizio));
            stmnt.setDate(4, Date.valueOf(dataFine));
            stmnt.setInt(5, idLotto);
            stmnt.executeUpdate();
            int idProgetto = stmnt.getInt(1);
            return new ProjectDTO(idProgetto, nome, dataInizio, dataFine, idLotto);
        } catch (SQLException ex) {
            if (ex.getSQLState().equalsIgnoreCase("P0002")) { // Codice SQLSTATE per violazioni di integrità specifiche
                throw new IllegalStateException("La data di inizio è successiva alla data di fine.");
            } else {
                throw new RuntimeException("Errore durante la creazione del progetto.", ex);
            }
        }
    }

    /**
     * Recupera una collezione di progetti associati a un lotto specifico.
     * @param idLotto L'ID del lotto per cui recuperare i progetti.
     * @return Una {@link Collection} di {@link ProjectDTO} che rappresenta i progetti del lotto.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero dei progetti.
     */
    public Collection<ProjectDTO> fetchProjectsByLot(int idLotto) {
        Set<ProjectDTO> progetti = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM progetto WHERE idlotto = ?");
            stmnt.setInt(1, idLotto);
            populateCollection(stmnt, progetti);
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero dei progetti per il lotto con ID " + idLotto, ex);
        }
        return progetti;
    }

    /**
     * Recupera tutti i progetti presenti nel database.
     * @return Una {@link Collection} di {@link ProjectDTO} che rappresenta tutti i progetti.
     * Restituisce una collezione vuota se non ci sono progetti o in caso di errore.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero di tutti i progetti.
     */
    public Collection<ProjectDTO> fetchAllProjects() {
        Set<ProjectDTO> progetti = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM progetto");
            populateCollection(stmnt, progetti);
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero di tutti i progetti.", ex);
        }
        return progetti;
    }

    /**
     * Popola una collezione di {@link ProjectDTO} eseguendo una query su un {@link PreparedStatement}.
     * Questo metodo è un'utility interna per evitare la duplicazione di codice.
     * @param stmnt Il {@link PreparedStatement} già configurato con la query.
     * @param progetti Il {@link Set} di {@link ProjectDTO} da popolare.
     * @throws SQLException se si verifica un errore SQL durante l'esecuzione della query o la lettura del ResultSet.
     */
    private static void populateCollection(PreparedStatement stmnt, Set<ProjectDTO> progetti) throws SQLException {
        ResultSet rs = stmnt.executeQuery();
        while (rs.next()) {
            progetti.add(new ProjectDTO(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getObject("datainizio", LocalDate.class),
                    rs.getObject("datafine", LocalDate.class),
                    rs.getInt("idlotto")
            ));
        }
    }

    /**
     * Recupera un progetto specifico dal database tramite il suo ID.
     * @param projectId L'ID del progetto da recuperare.
     * @return Un oggetto {@link ProjectDTO} che rappresenta il progetto trovato.
     * @throws IllegalStateException se nessun progetto con l'ID specificato viene trovato.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero del progetto.
     */
    public ProjectDTO fetchProjectById(int projectId) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM progetto WHERE id = ?");
            stmnt.setInt(1, projectId);
            ResultSet rs = stmnt.executeQuery();

            if (rs.next()) {
                return new ProjectDTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getObject("datainizio", LocalDate.class),
                        rs.getObject("datafine", LocalDate.class),
                        rs.getInt("idlotto")
                );
            } else {
                throw new IllegalStateException("Progetto con ID " + projectId + " non trovato.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero del progetto con ID " + projectId, ex);
        }
    }

    /**
     * Recupera un progetto specifico dal database dato l'ID di una coltivazione associata.
     * @param coltureId L'ID della coltivazione per cui recuperare il progetto padre.
     * @return Un oggetto {@link ProjectDTO} che rappresenta il progetto trovato.
     * @throws IllegalStateException se nessun progetto è associato all'ID della coltivazione specificato.
     * @throws RuntimeException se si verifica un errore SQL durante il recupero del progetto.
     */
    public ProjectDTO fetchProjectByColtureId(int coltureId) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT progetto.* FROM progetto INNER JOIN coltivazione ON coltivazione.idprogetto = progetto.id WHERE coltivazione.id = ?");
            stmnt.setInt(1, coltureId);
            ResultSet rs = stmnt.executeQuery();

            if (rs.next()) {
                return new ProjectDTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getObject("datainizio", LocalDate.class),
                        rs.getObject("datafine", LocalDate.class),
                        rs.getInt("idlotto")
                );
            } else {
                throw new IllegalStateException("Progetto con ID coltura " + coltureId + " non trovato.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero del progetto con ID coltura " + coltureId, ex);
        }
    }
}