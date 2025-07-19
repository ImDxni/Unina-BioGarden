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

public class ColtureDAO {
    private final DataSource dataSource = ConnectionManager.getDataSource();

    public void addColtura(int idProgetto, int idCrop) throws ColtureAlreadyExists {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmnt = conn.prepareCall("{? = call CreaColtivazione(?, ?, ?, ?)}");

            stmnt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmnt.setDate(2, Date.valueOf(LocalDate.now()));

            PGobject tipoUtenteObj = new PGobject();
            tipoUtenteObj.setType("StatoColtivazione");
            tipoUtenteObj.setValue(ColtureStatus.SEEDED.getStatus().toLowerCase());

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

    public Collection<ColtureDTO> fetchColtures(int projectId) {
        List<ColtureDTO> results = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM fetchColtures(?)");
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ColtureDTO colture = new ColtureDTO(
                        rs.getInt("id_coltivazione"),
                        rs.getDate("data_inizio").toLocalDate(),
                        ColtureStatus.fromString(rs.getString("stato")),
                        projectId,
                        rs.getInt("id_coltura"),
                        rs.getString("tipologia")
                );
                results.add(colture);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero delle colture: " + ex.getMessage(), ex);
        }

        return results;
    }
}
