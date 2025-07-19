package com.unina.biogarden.enumerations;

public enum ActivityStatus {
    PLANNED("pianificata"),
    IN_PROGRESS("in_corso"),
    COMPLETED("completata");

    private final String status;

    ActivityStatus(String status) {
        this.status = status;
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
