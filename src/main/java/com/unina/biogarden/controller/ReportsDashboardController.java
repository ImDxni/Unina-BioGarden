package com.unina.biogarden.controller;

import com.unina.biogarden.models.Lot;
import com.unina.biogarden.models.report.HarvestReportEntry;
import com.unina.biogarden.service.ProjectService;
import com.unina.biogarden.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.fx.ChartViewer; // Importa ChartViewer per JavaFX
import org.jfree.data.category.DefaultCategoryDataset;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ReportsDashboardController {

    @FXML
    private ComboBox<Lot> lotComboBox; // ComboBox per selezionare il lotto
    @FXML
    private TableView<HarvestReportEntry> harvestSummaryTable;
    @FXML
    private TableColumn<HarvestReportEntry, String> colLotName;
    @FXML
    private TableColumn<HarvestReportEntry, String> colCultivationName;
    @FXML
    private TableColumn<HarvestReportEntry, Integer> colTotalHarvests;
    @FXML
    private TableColumn<HarvestReportEntry, Double> colAvgQuantity;
    @FXML
    private TableColumn<HarvestReportEntry, Double> colMinQuantity;
    @FXML
    private TableColumn<HarvestReportEntry, Double> colMaxQuantity;
    @FXML
    private TableColumn<HarvestReportEntry, String> colUnit;
    @FXML
    private VBox chartContainer;

    private ProjectService projectService = new ProjectService();

    @FXML
    public void initialize() {
        // Configura le colonne della tabella
        colLotName.setCellValueFactory(new PropertyValueFactory<>("lotName"));
        colCultivationName.setCellValueFactory(new PropertyValueFactory<>("cultivationName"));
        colTotalHarvests.setCellValueFactory(new PropertyValueFactory<>("totalHarvests"));
        colAvgQuantity.setCellValueFactory(new PropertyValueFactory<>("avgQuantity"));
        colMinQuantity.setCellValueFactory(new PropertyValueFactory<>("minQuantity"));
        colMaxQuantity.setCellValueFactory(new PropertyValueFactory<>("maxQuantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));

        // Formattazione per le quantità decimali
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        colAvgQuantity.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : decimalFormat.format(item));
            }
        });
        colMinQuantity.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : decimalFormat.format(item));
            }
        });
        colMaxQuantity.setCellFactory(column -> new TableCell<HarvestReportEntry, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : decimalFormat.format(item));
            }
        });


        // Popola la ComboBox dei lotti
        try {
            Collection<Lot> lots = projectService.fetchAllLots();
            // Aggiungi un'opzione "Tutti i Lotti" all'inizio
            ObservableList<Lot> lotOptions = FXCollections.observableArrayList();
            lotOptions.add(new Lot(0, "Tutti i Lotti",0)); // Lotto fittizio per l'opzione "Tutti"
            lotOptions.addAll(lots);
            lotComboBox.setItems(lotOptions);
            lotComboBox.getSelectionModel().selectFirst(); // Seleziona "Tutti i Lotti" di default

            // Listener per la selezione del lotto
            lotComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loadReportData(newVal.getId() == 0 ? null : newVal.getId()); // Passa null se "Tutti i Lotti"
                }
            });

        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i lotti: " + e.getMessage());
            e.printStackTrace();
        }

        // Carica i dati iniziali (per tutti i lotti)
        loadReportData(null);
    }

    /**
     * Carica i dati del report di raccolta e aggiorna la tabella e il grafico.
     * @param lotId L'ID del lotto da filtrare, o null per tutti i lotti.
     */
    private void loadReportData(Integer lotId) {
        try {
            List<HarvestReportEntry> reportData = projectService.generateHarvestReport(lotId);
            harvestSummaryTable.setItems(FXCollections.observableArrayList(reportData));
            updateChart(reportData);
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Report", "Impossibile generare il report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna il grafico a barre con i dati del report.
     * @param reportData La lista di HarvestReportEntry da visualizzare nel grafico.
     */
    private void updateChart(List<HarvestReportEntry> reportData) {
        chartContainer.getChildren().clear(); // Pulisci il contenitore precedente

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        reportData.stream()
                .sorted(Comparator.comparing(HarvestReportEntry::getCultivationName))
                .forEach(entry -> {
                    dataset.addValue(entry.getAvgQuantity(), "Quantità Media", entry.getCultivationName());
                });

        JFreeChart barChart = ChartFactory.createBarChart(
                "Quantità Media Raccolta per Coltura",
                "Coltura",
                "Quantità Media",
                dataset
        );

        CategoryAxis domainAxis = barChart.getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        ChartViewer chartViewer = new ChartViewer(barChart);
        chartViewer.setPrefSize(chartContainer.getPrefWidth(), chartContainer.getPrefHeight());

        chartViewer.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);

        chartViewer.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) {
                event.consume();
            }
        });
        chartViewer.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, javafx.scene.input.MouseEvent::consume);
        chartViewer.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, javafx.scene.input.MouseEvent::consume);

        chartViewer.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
            }
        });

        chartContainer.getChildren().add(chartViewer);
    }
}