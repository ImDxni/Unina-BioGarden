package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.LottoDTO;
import com.unina.biogarden.session.Session;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LottoDAO {

    private final DataSource dataSource = ConnectionManager.getDataSource();


    public LottoDTO createPlot(String name, int area){
        int ownerID = Session.getUtente().id();
        try(Connection conn = dataSource.getConnection()){
            CallableStatement stmnt = conn.prepareCall("{ ? = call CreaLotto(?, ?,?) }");
            stmnt.registerOutParameter(1, java.sql.Types.INTEGER);

            stmnt.setString(2, name);
            stmnt.setInt(3, area);
            stmnt.setInt(4, ownerID);

            stmnt.executeUpdate();

            int plotID = stmnt.getInt(1);
            return new LottoDTO(plotID, name, area);
        }catch(SQLException ex){
            ex.printStackTrace();
        }

        return null;
    }

    public Collection<LottoDTO> getAllLots(){
        Set<LottoDTO> lots = new HashSet<>();
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM lotto WHERE idutente = ?");

            stmnt.setInt(1, Session.getUtente().id());
            ResultSet rs = stmnt.executeQuery();

            while(rs.next()){
                lots.add(new LottoDTO(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("area")
                ));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }

        return lots;
    }


}
