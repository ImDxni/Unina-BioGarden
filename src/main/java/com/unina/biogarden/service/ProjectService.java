package com.unina.biogarden.service;

import com.unina.biogarden.dao.*;
import com.unina.biogarden.dto.LotDTO;
import com.unina.biogarden.dto.ProjectDTO;
import com.unina.biogarden.dto.UserDTO;
import com.unina.biogarden.dto.activity.ActivityDTO;
import com.unina.biogarden.dto.activity.HarvestingActivityDTO;
import com.unina.biogarden.dto.activity.IrrigationActivityDTO;
import com.unina.biogarden.dto.activity.SeedingActivityDTO;
import com.unina.biogarden.enumerations.ActivityType;
import com.unina.biogarden.exceptions.ColtureAlreadyExists;
import com.unina.biogarden.models.Colture;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.models.Lot;
import com.unina.biogarden.models.Project;
import com.unina.biogarden.models.activity.Activity;
import com.unina.biogarden.models.activity.HarvestingActivity;
import com.unina.biogarden.models.activity.IrrigationActivity;
import com.unina.biogarden.models.activity.SeedingActivity;
import com.unina.biogarden.models.report.HarvestReportEntry;

import java.util.*;

/**
 * Servizio per la gestione dei progetti nel sistema BioGarden.
 * Questa classe si occupa della logica di business relativa ai progetti,
 * coordinando le operazioni tra i vari Data Access Objects (DAO)
 * per la creazione, il recupero, l'aggiornamento e la gestione delle entità
 * come progetti, lotti, colture e attività.
 * Estende {@link AbstractService} per fornire operazioni CRUD di base sui {@link ProjectDTO}.
 * @author Il Tuo Nome
 */
public class ProjectService extends AbstractService<ProjectDTO> {
    private final ProjectDAO projectDao = new ProjectDAO();
    private final LotDAO lotDao = new LotDAO();
    private final CropDAO cropDao = new CropDAO();
    private final ColtureDAO coltureDao = new ColtureDAO();

    private final ActivityDAO activityDAO = new ActivityDAO();


    /**
     * Inserisce un nuovo progetto nel database.
     * Utilizza {@link ProjectDAO#creaProgetto(String, java.time.LocalDate, java.time.LocalDate, int)}
     * per persistere i dati del {@link ProjectDTO}.
     *
     * @param entity Il {@link ProjectDTO} contenente i dati del progetto da creare.
     * @return Il {@link ProjectDTO} del progetto creato, con l'ID generato dal database.
     * @throws RuntimeException Se si verifica un errore durante l'operazione di creazione nel database.
     */
    @Override
    public ProjectDTO insert(ProjectDTO entity) {
        return projectDao.creaProgetto(
                entity.nome(),
                entity.dataInizio(),
                entity.dataFine(),
                entity.idLotto()
        );
    }

    /**
     * Recupera tutti i progetti dal database.
     * Utilizza {@link ProjectDAO#fetchAllProjects()} per ottenere tutti i {@link ProjectDTO}.
     *
     * @return Una collezione di tutti i {@link ProjectDTO} presenti nel database.
     */
    @Override
    public Collection<ProjectDTO> fetchAll() {
        return projectDao.fetchAllProjects();
    }


    /**
     * Aggiunge una nuova attività a una specifica coltivazione.
     * Il tipo di attività determina quale DTO specifico verrà creato e inviato al DAO.
     *
     * @param colture L'oggetto {@link Colture} a cui aggiungere l'attività.
     * @param activity L'oggetto {@link Activity} da aggiungere. L'ID verrà generato dal database.
     * @throws IllegalStateException Se la coltivazione non è associata a nessun progetto.
     * @throws IllegalArgumentException Se il tipo di attività non è supportato.
     */
    public void addActivityToColture(Colture colture, Activity activity) {
        ProjectDTO projectDTO = projectDao.fetchProjectByColtureId(colture.getId());
        if (projectDTO == null) {
            throw new IllegalStateException("Colture non associata a nessun progetto");
        }

        ActivityDTO dto;
        switch (activity.getType()) {
            case SEEDING -> {
                SeedingActivity seedingActivity = (SeedingActivity) activity;
                dto = new SeedingActivityDTO(
                        0, // ID will be generated by the database
                        activity.getDate(),
                        activity.getStatus(),
                        seedingActivity.getQuantity(),
                        seedingActivity.getUnit(),
                        colture.getId(),
                        projectDTO.idLotto(),
                        activity.getFarmerID()
                );
            }
            case IRRIGATION -> {
                dto = new IrrigationActivityDTO(
                        0, // ID will be generated by the database
                        activity.getDate(),
                        activity.getStatus(),
                        colture.getId(),
                        projectDTO.idLotto(),
                        activity.getFarmerID()
                );
            }
            case HARVEST -> {
                HarvestingActivity harvestingActivity = (HarvestingActivity) activity;
                dto = new HarvestingActivityDTO(
                        0, // ID will be generated by the database
                        activity.getDate(),
                        activity.getStatus(),
                        harvestingActivity.getPlannedQuantity(),
                        harvestingActivity.getActualQuantity(),
                        harvestingActivity.getUnit(),
                        colture.getId(),
                        projectDTO.idLotto(),
                        activity.getFarmerID()
                );
            }
            default -> {
                throw new IllegalArgumentException("Tipo di attività non supportato: " + activity.getType());
            }
        }

        activityDAO.insertActivity(dto);
    }

    /**
     * Aggiunge una nuova coltivazione a un progetto esistente.
     *
     * @param project L'oggetto {@link Project} a cui aggiungere la coltivazione.
     * @param crop L'oggetto {@link Crop} che rappresenta il tipo di coltura da aggiungere.
     * @throws ColtureAlreadyExists Se la coltivazione esiste già per quel progetto.
     */
    public void addColture(Project project, Crop crop) throws ColtureAlreadyExists {
        coltureDao.addColtura(project.getId(), crop.getId());
    }


    /**
     * Crea un nuovo lotto nel database.
     *
     * @param name Il nome del lotto.
     * @param area L'area del lotto in metri quadrati.
     */
    public void createLot(String name, int area) {
        lotDao.createPlot(name, area);
    }

    /**
     * Crea un nuovo tipo di coltura nel database.
     *
     * @param name Il nome del tipo di coltura (es. "Pomodoro").
     * @param growingTIme Il tempo di maturazione in giorni.
     */
    public void createCrop(String name, int growingTIme) {
        cropDao.creaColtura(name, growingTIme);
    }

    /**
     * Recupera tutte le attività associate a una specifica coltivazione.
     * Le attività vengono mappate da {@link ActivityDTO} a oggetti {@link Activity} concreti.
     *
     * @param coltureId L'ID della coltivazione di cui recuperare le attività.
     * @return Una collezione di oggetti {@link Activity} associati alla coltivazione specificata.
     */
    public Collection<Activity> fetchActivities(int coltureId) {
        return activityDAO.fetchActivityByColture(coltureId).stream()
                .map(activity -> {
                    String farmerFullName = getFarmerById(activity.getFarmerID()).getFullName();
                    return switch (activity.getType()) {
                        case SEEDING -> new SeedingActivity(
                                activity.getId(),
                                activity.getDate(),
                                activity.getStatus(),
                                activity.getFarmerID(),
                                farmerFullName,
                                ((SeedingActivityDTO) activity).getQuantity(),
                                ((SeedingActivityDTO) activity).getUnit()
                        );
                        case HARVEST -> new HarvestingActivity(
                                activity.getId(),
                                activity.getDate(),
                                activity.getStatus(),
                                activity.getFarmerID(),
                                farmerFullName,
                                ((HarvestingActivityDTO) activity).getExpectedQuantity(),
                                ((HarvestingActivityDTO) activity).getActualQuantity(),
                                ((HarvestingActivityDTO) activity).getUnit()
                        );
                        case IRRIGATION -> new IrrigationActivity(
                                activity.getId(),
                                activity.getDate(),
                                activity.getStatus(),
                                activity.getFarmerID(),
                                farmerFullName
                        );
                    };
                }).toList();
    }

    /**
     * Elimina un'attività dal database.
     *
     * @param activity L'oggetto {@link Activity} da eliminare (l'eliminazione avviene tramite l'ID dell'attività).
     */
    public void deleteActivity(Activity activity) {
        activityDAO.deleteActivity(activity.getId());
    }

    /**
     * Recupera tutte le coltivazioni associate a un progetto specifico.
     *
     * @param projectId L'ID del progetto di cui recuperare le coltivazioni.
     * @return Una collezione di oggetti {@link Colture} associati al progetto specificato.
     */
    public Collection<Colture> getColtures(int projectId) {
        return coltureDao.fetchColtures(projectId).stream()
                .map(colture -> new Colture(
                        colture.id(), colture.startDate(),
                        colture.status(),
                        // Si crea un Crop parziale qui, poiché CropDAO sarebbe necessario per i dettagli completi
                        new Crop(0, colture.cropName(), 0, 0)
                )).toList();
    }

    /**
     * Recupera tutti i tipi di coltura disponibili.
     *
     * @return Una collezione di oggetti {@link Crop} che rappresentano tutti i tipi di coltura.
     */
    public Collection<Crop> getCrops() {
        return cropDao.fetchAllCrop().stream()
                .map(crop -> new Crop(crop.id(), crop.nome(), crop.giorniMaturazione(), cropDao.countCropProjects(crop.id())))
                .toList();
    }

    /**
     * Recupera tutti i progetti esistenti, popolando i dettagli del lotto associato.
     *
     * @return Una collezione di oggetti {@link Project} completi di dettagli del lotto.
     */
    public Collection<Project> getProjects() {
        Collection<ProjectDTO> projects = projectDao.fetchAllProjects();

        return projects.stream()
                .map(project -> {
                    LotDTO lotDTO = lotDao.getLotById(project.idLotto());
                    return
                            new Project(
                                    project.id(),
                                    project.nome(),
                                    lotDTO.nome(),
                                    project.dataInizio(),
                                    project.dataFine()
                            );
                }).toList();
    }

    /**
     * Recupera i progetti associati a un lotto specifico.
     *
     * @param lot L'oggetto {@link Lot} di cui recuperare i progetti.
     * @return Una collezione di oggetti {@link Project} associati al lotto specificato.
     */
    public Collection<Project> fetchProjectByLot(Lot lot) {
        Collection<ProjectDTO> projects = projectDao.fetchProjectsByLot(lot.getId());

        return projects.stream()
                .map(project -> {
                    LotDTO lotDTO = lotDao.getLotById(project.idLotto());
                    return
                            new Project(
                                    project.id(),
                                    project.nome(),
                                    lotDTO.nome(),
                                    project.dataInizio(),
                                    project.dataFine()
                            );
                }).toList();
    }

    /**
     * Recupera un singolo progetto tramite il suo ID.
     *
     * @param projectId L'ID del progetto da recuperare.
     * @return L'oggetto {@link Project} corrispondente all'ID.
     */
    public Project fetchProjectById(int projectId) {
        ProjectDTO project = projectDao.fetchProjectById(projectId);
        Lot lot = new Lot(lotDao.getLotById(project.idLotto()));

        return new Project(
                project.id(),
                project.nome(),
                lot.getName(),
                project.dataInizio(),
                project.dataFine()
        );
    }

    /**
     * Recupera tutti i lotti esistenti.
     *
     * @return Una collezione di oggetti {@link Lot}.
     */
    public Collection<Lot> fetchAllLots() {
        Collection<LotDTO> lots = lotDao.getAllLots();

        return lots.stream()
                .map(Lot::new)
                .toList();
    }

    /**
     * Metodo privato per ottenere i dettagli di un agricoltore tramite il suo ID.
     * Utilizzato per popolare il nome completo dell'agricoltore nelle attività.
     *
     * @param farmerId L'ID dell'agricoltore.
     * @return L'oggetto {@link UserDTO} che rappresenta l'agricoltore.
     * @throws IllegalArgumentException Se l'agricoltore con l'ID specificato non viene trovato.
     */
    private UserDTO getFarmerById(int farmerId) {
        return UserService.getUsers().stream()
                .filter(user -> user.id() == farmerId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Farmer not found with ID: " + farmerId));
    }

    /**
     * Aggiorna un'attività esistente nel database.
     * Il tipo di attività determina quale DTO specifico verrà creato e inviato al DAO.
     *
     * @param currentActivity L'oggetto {@link Activity} da aggiornare.
     * @throws IllegalArgumentException Se il tipo di attività non è supportato.
     */
    public void updateActivity(Activity currentActivity) {
        ActivityDTO activityDTO;
        switch (currentActivity.getType()) {
            case SEEDING -> {
                SeedingActivity seedingActivity = (SeedingActivity) currentActivity;
                activityDTO = new SeedingActivityDTO(
                        currentActivity.getId(),
                        currentActivity.getDate(),
                        currentActivity.getStatus(),
                        seedingActivity.getQuantity(),
                        seedingActivity.getUnit(),
                        0, // Colture ID will be set later
                        0, // Lot ID will be set later
                        currentActivity.getFarmerID()
                );
            }
            case IRRIGATION -> {
                activityDTO = new IrrigationActivityDTO(
                        currentActivity.getId(),
                        currentActivity.getDate(),
                        currentActivity.getStatus(),
                        0, // Colture ID will be set later
                        0, // Lot ID will be set later
                        currentActivity.getFarmerID()
                );
            }
            case HARVEST -> {
                HarvestingActivity harvestingActivity = (HarvestingActivity) currentActivity;
                activityDTO = new HarvestingActivityDTO(
                        currentActivity.getId(),
                        currentActivity.getDate(),
                        currentActivity.getStatus(),
                        harvestingActivity.getPlannedQuantity(),
                        harvestingActivity.getActualQuantity(),
                        harvestingActivity.getUnit(),
                        0, // Colture ID will be set later
                        0, // Lot ID will be set later
                        currentActivity.getFarmerID()
                );
            }
            default -> throw new IllegalArgumentException("Unsupported activity type: " + currentActivity.getType());
        }

        activityDAO.updateActivity(activityDTO);
    }

    /**
     * Genera un report riassuntivo sulle attività di raccolta, facoltativamente filtrato per un lotto specifico.
     * Il report include statistiche come il numero totale di raccolte, la quantità media, minima e massima.
     *
     * @param lotId L'ID del lotto per filtrare il report. Se {@code null}, il report includerà dati da tutti i lotti.
     * @return Una {@link List} di {@link HarvestReportEntry} contenente le statistiche di raccolta.
     */
    public List<HarvestReportEntry> generateHarvestReport(Integer lotId) {
        List<HarvestReportEntry> reportEntries = new ArrayList<>();

        // Filtra i progetti per lotto se specificato
        Collection<Project> projectsToConsider = lotId == null ? getProjects() : fetchProjectByLot(new Lot(lotDao.getLotById(lotId)));


        for (Project project : projectsToConsider) {
            for (Colture cultivation : getColtures(project.getId())) {
                // Filtra le attività di raccolta per questa coltura
                List<HarvestingActivity> harvestActivities = fetchActivities(cultivation.getId()).stream()
                        .filter(activity -> activity.getType() == ActivityType.HARVEST)
                        .map(activity -> (HarvestingActivity) activity)
                        .toList();

                if (!harvestActivities.isEmpty()) {
                    int totalHarvests = harvestActivities.size();
                    double sumQuantities = harvestActivities.stream()
                            .mapToDouble(HarvestingActivity::getActualQuantity)
                            .sum();
                    double avgQuantity = sumQuantities / totalHarvests;

                    Optional<Integer> minQuantity = harvestActivities.stream()
                            .map(HarvestingActivity::getActualQuantity)
                            .min(Comparator.naturalOrder());

                    Optional<Integer> maxQuantity = harvestActivities.stream()
                            .map(HarvestingActivity::getActualQuantity)
                            .max(Comparator.naturalOrder());

                    // Assumiamo che l'unità di misura sia la stessa per tutte le raccolte di una coltura
                    // O prendiamo quella della prima attività
                    String unit = harvestActivities.get(0).getUnit();

                    String currentLotName;
                    if (lotId == null) {
                        // Se lotId è null, significa che stiamo generando il report per tutti i lotti.
                        // Per ogni entry, dobbiamo trovare il nome del lotto specifico del progetto.
                        currentLotName = project.lotProperty().get();
                    } else {
                        // Se lotId è specificato, tutti i progetti considerati appartengono a quel lotto.
                        // Quindi possiamo semplicemente prendere il nome del lotto specificato.
                        currentLotName = lotDao.getLotById(lotId).nome();
                    }


                    reportEntries.add(new HarvestReportEntry(
                            currentLotName,
                            cultivation.getCrop().nameProperty().get(),
                            totalHarvests,
                            avgQuantity,
                            minQuantity.orElse(0).doubleValue(), // Gestisce il caso di Optional vuoto
                            maxQuantity.orElse(0).doubleValue(), // Gestisce il caso di Optional vuoto
                            unit
                    ));
                }
            }
        }
        return reportEntries;
    }
}