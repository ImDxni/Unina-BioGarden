package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.CropDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Data Access Object (DAO) per la gestione delle operazioni CRUD relative ai tipi di coltura (Crop) nel database.
 * Questa classe fornisce metodi per creare nuovi tipi di coltura e per recuperare tutti i tipi di coltura esistenti.
 * Utilizza {@link ConnectionManager} per ottenere connessioni al database.
 * @author Il Tuo Nome
 */
public class CropDAO {

    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Crea un nuovo tipo di coltura nel database.
     * @param tipologia La tipologia (nome) della coltura da creare.
     * @param tempoMaturazione Il tempo di maturazione in giorni per questa coltura.
     * @return Un oggetto {@link CropDTO} che rappresenta la coltura appena creata, con l'ID assegnato dal database.
     * @throws IllegalStateException se una coltura con la stessa tipologia esiste già nel database.
     * @throws RuntimeException se si verifica un errore SQL generico durante la creazione della coltura.
     */
    public CropDTO creaColtura(String tipologia, int tempoMaturazione) {
        try (Connection conn = dataSource.getConnection()) {

            CallableStatement stmnt = conn.prepareCall("{ ? = call CreaColtura(?, ?) }");
            stmnt.registerOutParameter(1, java.sql.Types.INTEGER); // Parametro di ritorno (ID)
            stmnt.setString(2, tipologia);
            stmnt.setInt(3, tempoMaturazione);
            stmnt.execute();

            int idColtura = stmnt.getInt(1);
            return new CropDTO(idColtura, tipologia, tempoMaturazione);

        } catch (SQLException ex) {
            // Codice di stato SQL "23505" indica una violazione di chiave unica (es. nome duplicato)
            if (ex.getSQLState().equalsIgnoreCase("23505")) {
                throw new IllegalStateException("La coltura '" + tipologia + "' esiste già.");
            } else {
                throw new RuntimeException("Errore durante la creazione della coltura: " + ex.getMessage(), ex);
            }
        }
    }

    /**
     * Recupera tutti i tipi di coltura (Crop) presenti nel database.
     * @return Una {@link Collection} di oggetti {@link CropDTO} che rappresentano tutti i tipi di coltura.
     * Restituisce una collezione vuota se non ci sono colture o in caso di errore.
     */
    public Collection<CropDTO> fetchAllCrop() {
        Set<CropDTO> colture = new HashSet<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM Coltura");
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                colture.add(new CropDTO(
                        rs.getInt("id"),
                        rs.getString("tipologia"),
                        rs.getInt("tempomaturazione")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Errore durante il recupero di tutti i tipi di coltura: " + ex.getMessage());
            ex.printStackTrace(); // Log dell'eccezione per il debug
        }
        return colture;
    }
}