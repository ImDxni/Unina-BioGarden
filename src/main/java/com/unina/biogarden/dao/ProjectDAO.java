package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.ProjectDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProjectDAO {

    private final DataSource dataSource = ConnectionManager.getDataSource();

    public ProjectDTO creaProgetto(String nome, LocalDate dataInizio, LocalDate dataFine, int idLotto) {
        try(Connection conn = dataSource.getConnection()){
            CallableStatement stmnt = conn.prepareCall("{ ? = call CreaProgetto(?, ?, ?, ?) }");
            stmnt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmnt.setString(2, nome);
            stmnt.setDate(3, Date.valueOf(dataInizio));
            stmnt.setDate(4, Date.valueOf(dataFine));
            stmnt.setInt(5, idLotto);
            stmnt.executeUpdate();
            int idProgetto = stmnt.getInt(1);
            return new ProjectDTO(idProgetto, nome, dataInizio, dataFine, idLotto);
        }catch(SQLException ex){
            if(ex.getSQLState().equalsIgnoreCase("P0002")){
                throw new IllegalStateException("La data di inizio è successiva alla data di fine");
            } else {
                throw new RuntimeException("Errore durante la creazione del progetto", ex);
            }
        }
    }

    public Collection<ProjectDTO> fetchProjectsByLot(int idLotto) {
        Set<ProjectDTO> progetti = new HashSet<>();
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM progetto WHERE idlotto = ?");

            stmnt.setInt(1,idLotto);

            populateCollection(stmnt, progetti);
        }catch(SQLException ex){
            throw new RuntimeException("Errore durante il recupero dei progetti per il lotto con ID " + idLotto, ex);
        }
        return progetti;
    }



    public Collection<ProjectDTO> fetchAllProjects(){
        Set<ProjectDTO> progetti = new HashSet<>();
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM progetto");

            populateCollection(stmnt, progetti);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return progetti;
    }


    private static void populateCollection(PreparedStatement stmnt, Set<ProjectDTO> progetti) throws SQLException {
        ResultSet rs = stmnt.executeQuery();

        while(rs.next()){
            progetti.add(new ProjectDTO(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getObject("datainizio", LocalDate.class),
                    rs.getObject("datafine", LocalDate.class),
                    rs.getInt("idlotto")
            ));
        }
    }

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
