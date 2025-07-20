package com.unina.biogarden.enumerations;

public enum ColtureStatus {
    WAITING("attesa"),
    SEEDED("seminato"),
    GROWED("maturo"),
    HARVESTED("raccolto");

    private final String status;

    ColtureStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ColtureStatus fromString(String status) {
        for (ColtureStatus coltureStatus : ColtureStatus.values()) {
            if (coltureStatus.getStatus().equalsIgnoreCase(status)) {
                return coltureStatus;
            }
        }
        throw new IllegalArgumentException("Unknown colture status: " + status);
    }
}
