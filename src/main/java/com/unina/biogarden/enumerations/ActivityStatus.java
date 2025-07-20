package com.unina.biogarden.enumerations;

public enum ActivityStatus {
    PLANNED("pianificata","Pianificata"),
    IN_PROGRESS("in_corso", "In Corso"),
    COMPLETED("terminata", "Completata");

    private final String status,label;

    ActivityStatus(String status,String label) {
        this.status = status;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getStatus() {
        return status;
    }

    public static ActivityStatus fromString(String status) {
        for (ActivityStatus activityStatus : ActivityStatus.values()) {
            if (activityStatus.getStatus().equalsIgnoreCase(status)) {
                return activityStatus;
            }
        }
        throw new IllegalArgumentException("Unknown activity status: " + status);
    }
}
