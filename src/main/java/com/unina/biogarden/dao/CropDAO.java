package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.CropDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CropDAO {

    private final DataSource dataSource = ConnectionManager.getDataSource();

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
            if (ex.getSQLState().equalsIgnoreCase("23505")) {
                throw new IllegalStateException("La coltura '" + tipologia + "' esiste già.");
            } else {
                throw new RuntimeException("Errore durante la creazione della coltura: " + ex.getMessage(), ex);
            }
        }
    }

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
            ex.printStackTrace();
        }
        return colture;
    }
}