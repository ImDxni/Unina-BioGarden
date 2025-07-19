package com.unina.biogarden.enumerations;

public enum ActivityType {
    SEEDING("semina"),
    IRRIGATION("irrigazione"),
    HARVEST("raccolta");

    private final String description;
    ActivityType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public static ActivityType fromString(String description) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getDescription().equalsIgnoreCase(description)) {
                return activityType;
            }
        }
        throw new IllegalArgumentException("Unknown activity type: " + description);
    }
}
