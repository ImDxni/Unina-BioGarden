package com.unina.biogarden.models.report;

public class HarvestReportEntry {
    private String lotName;
    private String cultivationName;
    private int totalHarvests;
    private double avgQuantity;
    private double minQuantity;
    private double maxQuantity;
    private String unit; // Assumiamo che l'unità sia la stessa per tutte le raccolte di una coltura

    public HarvestReportEntry(String lotName, String cultivationName, int totalHarvests, double avgQuantity, double minQuantity, double maxQuantity, String unit) {
        this.lotName = lotName;
        this.cultivationName = cultivationName;
        this.totalHarvests = totalHarvests;
        this.avgQuantity = avgQuantity;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.unit = unit;
    }

    // Getters (necessari per TableView)
    public String getLotName() { return lotName; }
    public String getCultivationName() { return cultivationName; }
    public int getTotalHarvests() { return totalHarvests; }
    public double getAvgQuantity() { return avgQuantity; }
    public double getMinQuantity() { return minQuantity; }
    public double getMaxQuantity() { return maxQuantity; }
    public String getUnit() { return unit; }

    // Setters (se necessari, ma non per questo specifico report)
    public void setLotName(String lotName) { this.lotName = lotName; }
    public void setCultivationName(String cultivationName) { this.cultivationName = cultivationName; }
    public void setTotalHarvests(int totalHarvests) { this.totalHarvests = totalHarvests; }
    public void setAvgQuantity(double avgQuantity) { this.avgQuantity = avgQuantity; }
    public void setMinQuantity(double minQuantity) { this.minQuantity = minQuantity; }
    public void setMaxQuantity(double maxQuantity) { this.maxQuantity = maxQuantity; }
    public void setUnit(String unit) { this.unit = unit; }
}