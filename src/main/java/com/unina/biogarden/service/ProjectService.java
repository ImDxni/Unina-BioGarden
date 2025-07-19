package com.unina.biogarden.service;

import com.unina.biogarden.dao.ColtureDAO;
import com.unina.biogarden.dao.CropDAO;
import com.unina.biogarden.dao.LotDAO;
import com.unina.biogarden.dao.ProjectDAO;
import com.unina.biogarden.dto.LotDTO;
import com.unina.biogarden.dto.ProjectDTO;
import com.unina.biogarden.exceptions.ColtureAlreadyExists;
import com.unina.biogarden.models.Colture;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.models.Lot;
import com.unina.biogarden.models.Project;

import java.util.Collection;

public class ProjectService extends AbstractService<ProjectDTO> {
    private final ProjectDAO projectDao = new ProjectDAO();
    private final LotDAO lotDao = new LotDAO();
    private final CropDAO cropDao = new CropDAO();
    private final ColtureDAO coltureDao = new ColtureDAO();


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

        return projectDao.fetchAllProjects();
    }


    public void addColture(Project project, Crop crop) throws ColtureAlreadyExists {
        coltureDao.addColtura(project.getId(),crop.getId());
    }

    public void createLot(String name, int area) {
        lotDao.createPlot(name, area);
    }

    public void createCrop(String name, int growingTIme) {
        cropDao.creaColtura(name, growingTIme);
    }


    public Collection<Colture> getColtures(int projectId){
        return coltureDao.fetchColtures(projectId).stream()
                .map(colture -> new Colture(
                        colture.startDate(),
                        colture.status(),
                        new Crop(0,colture.cropName(),0)
                )).toList();
    }

    public Collection<Crop> getCrops() {
        return cropDao.fetchAllCrop().stream()
                .map(crop -> new Crop(crop.id(),crop.nome(), crop.giorniMaturazione()))
                .toList();
    }

    public Collection<Project> getProjects(){
        Collection<ProjectDTO> projects = projectDao.fetchAllProjects();

        return projects.stream()
                .map(project -> {
                    LotDTO lotDTO = lotDao.getLotById(project.idLotto());
                    return
                    new Project(
                            project.id(),
                            project.nome(),
                            lotDTO.id(),
                            lotDTO.nome(),
                            project.dataInizio(),
                            project.dataFine()
                    );
                }).toList();
    }


    public Collection<Project> fetchProjectByLot(Lot lot){
        Collection<ProjectDTO> projects = projectDao.fetchProjectsByLot(lot.getId());

        return projects.stream()
                .map(project -> {
                    LotDTO lotDTO = lotDao.getLotById(project.idLotto());
                    return
                            new Project(
                                    project.id(),
                                    project.nome(),
                                    lotDTO.id(),
                                    lotDTO.nome(),
                                    project.dataInizio(),
                                    project.dataFine()
                            );
                }).toList();
    }

    public Project fetchProjectById(int projectId){
        ProjectDTO project = projectDao.fetchProjectById(projectId);
        Lot lot = new Lot(lotDao.getLotById(project.idLotto()));

        return new Project(
                project.id(),
                project.nome(),
                lot.getId(),
                lot.getName(),
                project.dataInizio(),
                project.dataFine()
        );
    }

    public Collection<Lot> fetchAllLots() {
        Collection<LotDTO> lots = lotDao.getAllLots();

        return lots.stream()
                .map(Lot::new)
                .toList();

    }
}
