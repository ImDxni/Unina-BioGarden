package com.unina.biogarden.service;

import com.unina.biogarden.dao.CropDAO;
import com.unina.biogarden.dao.LotDAO;
import com.unina.biogarden.dao.ProjectDAO;
import com.unina.biogarden.dto.LotDTO;
import com.unina.biogarden.dto.ProjectDTO;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.models.Lot;
import com.unina.biogarden.models.Project;

import java.util.Collection;

public class ProjectService extends AbstractService<ProjectDTO> {
    private final ProjectDAO projectDao = new ProjectDAO();
    private final LotDAO lotDao = new LotDAO();
    private final CropDAO cropDao = new CropDAO();


    @Override
    public ProjectDTO insert(ProjectDTO entity) {
        return projectDao.creaProgetto(
                entity.nome(),
                entity.dataInizio(),
                entity.dataFine(),
                entity.idLotto()
        );
    }

    @Override
    public Collection<ProjectDTO> fetchAll() {
        Collection<ProjectDTO> projects = projectDao.fetchAllProjects();

        return projects;
    }

    public void createLot(String name, int area) {
        lotDao.createPlot(name, area);
    }

    public void createCrop(String name, int growingTIme) {
        cropDao.creaColtura(name, growingTIme);
    }

    public Collection<Crop> getCrops() {
        return cropDao.fetchAllColture().stream()
                .map(crop -> new Crop(crop.nome(), crop.giorniMaturazione()))
                .toList();
    }

    public Collection<Project> getProjects(){
        Collection<ProjectDTO> projects = projectDao.fetchAllProjects();

        return projects.stream()
                .map(project -> new Project(
                        project.nome(),
                        lotDao.getLotById(project.idLotto()).nome(),
                        project.dataInizio(),
                        project.dataFine()
                )).toList();
    }


    public Collection<Project> fetchProjectByLot(Lot lot){
        Collection<ProjectDTO> projects = projectDao.fetchProjectsByLot(lot.getId());

        return projects.stream()
                .map(project -> new Project(
                        project.nome(),
                        lot.getName(),
                        project.dataInizio(),
                        project.dataFine()
                )).toList();
    }

    public Collection<Lot> fetchAllLots() {
        Collection<LotDTO> lots = lotDao.getAllLots();

        return lots.stream()
                .map(Lot::new)
                .toList();

    }
}
