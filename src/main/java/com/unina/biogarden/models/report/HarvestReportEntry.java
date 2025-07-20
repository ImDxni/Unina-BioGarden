package com.unina.biogarden.models.report;

/**
 * Rappresenta una singola riga di dati all'interno di un report riassuntivo sulle raccolte.
 * Questa classe aggrega statistiche chiave per una specifica combinazione di lotto e coltivazione,
 * includendo il numero totale di raccolte, la quantità media, minima e massima raccolta,
 * e l'unità di misura associata.
 * È pensata per essere utilizzata, ad esempio, in una {@code TableView} di una GUI per visualizzare i report.
 * @author Il Tuo Nome
 */
public class HarvestReportEntry {
    private String lotName;
    private String cultivationName;
    private int totalHarvests;
    private double avgQuantity;
    private double minQuantity;
    private double maxQuantity;
    private String unit; // Assumiamo che l'unità sia la stessa per tutte le raccolte di una coltura

    /**
     * Costruisce una nuova istanza di {@code HarvestReportEntry}.
     *
     * @param lotName Il nome del lotto a cui si riferiscono le raccolte.
     * @param cultivationName Il nome della coltivazione specifica (es. "Pomodoro San Marzano").
     * @param totalHarvests Il numero totale di raccolte effettuate per questa coltivazione nel lotto.
     * @param avgQuantity La quantità media raccolta per singola operazione di raccolta.
     * @param minQuantity La quantità minima raccolta in una singola operazione.
     * @param maxQuantity La quantità massima raccolta in una singola operazione.
     * @param unit L'unità di misura delle quantità (es. "kg", "pezzi"). Si assume che sia consistente per tutte le raccolte della stessa coltura.
     */
    public HarvestReportEntry(String lotName, String cultivationName, int totalHarvests, double avgQuantity, double minQuantity, double maxQuantity, String unit) {
        this.lotName = lotName;
        this.cultivationName = cultivationName;
        this.totalHarvests = totalHarvests;
        this.avgQuantity = avgQuantity;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.unit = unit;
    }

    /**
     * Restituisce il nome del lotto.
     * @return Il nome del lotto.
     */
    public String getLotName() {
        return lotName;
    }

    /**
     * Restituisce il nome della coltivazione.
     * @return Il nome della coltivazione.
     */
    public String getCultivationName() {
        return cultivationName;
    }

    /**
     * Restituisce il numero totale di operazioni di raccolta.
     * @return Il conteggio totale delle raccolte.
     */
    public int getTotalHarvests() {
        return totalHarvests;
    }

    /**
     * Restituisce la quantità media raccolta.
     * @return La quantità media.
     */
    public double getAvgQuantity() {
        return avgQuantity;
    }

    /**
     * Restituisce la quantità minima raccolta.
     * @return La quantità minima.
     */
    public double getMinQuantity() {
        return minQuantity;
    }

    /**
     * Restituisce la quantità massima raccolta.
     * @return La quantità massima.
     */
    public double getMaxQuantity() {
        return maxQuantity;
    }

    /**
     * Restituisce l'unità di misura delle quantità.
     * @return L'unità di misura.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Imposta il nome del lotto.
     * @param lotName Il nuovo nome del lotto.
     */
    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    /**
     * Imposta il nome della coltivazione.
     * @param cultivationName Il nuovo nome della coltivazione.
     */
    public void setCultivationName(String cultivationName) {
        this.cultivationName = cultivationName;
    }

    /**
     * Imposta il numero totale di operazioni di raccolta.
     * @param totalHarvests Il nuovo conteggio totale delle raccolte.
     */
    public void setTotalHarvests(int totalHarvests) {
        this.totalHarvests = totalHarvests;
    }

    /**
     * Imposta la quantità media raccolta.
     * @param avgQuantity La nuova quantità media.
     */
    public void setAvgQuantity(double avgQuantity) {
        this.avgQuantity = avgQuantity;
    }

    /**
     * Imposta la quantità minima raccolta.
     * @param minQuantity La nuova quantità minima.
     */
    public void setMinQuantity(double minQuantity) {
        this.minQuantity = minQuantity;
    }

    /**
     * Imposta la quantità massima raccolta.
     * @param maxQuantity La nuova quantità massima.
     */
    public void voidSetMaxQuantity(double maxQuantity) { // Corretto il nome del metodo
        this.maxQuantity = maxQuantity;
    }

    /**
     * Imposta l'unità di misura delle quantità.
     * @param unit La nuova unità di misura.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}