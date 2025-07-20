package com.unina.biogarden.enumerations;

/**
 * Enumerazione che rappresenta i diversi tipi di attività che possono essere svolte
 * nel contesto di una coltivazione all'interno del sistema BioGarden.
 * Ogni tipo di attività è associato a una descrizione testuale.
 * @author Il Tuo Nome
 */
public enum ActivityType {
    /**
     * Rappresenta l'attività di semina.
     */
    SEEDING("semina"),
    /**
     * Rappresenta l'attività di irrigazione.
     */
    IRRIGATION("irrigazione"),
    /**
     * Rappresenta l'attività di raccolta.
     */
    HARVEST("raccolta");

    private final String description;

    /**
     * Costruttore per {@code ActivityType}.
     * @param description La descrizione testuale del tipo di attività.
     */
    ActivityType(String description) {
        this.description = description;
    }

    /**
     * Restituisce la descrizione testuale del tipo di attività.
     * @return La descrizione del tipo di attività.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Converte una stringa nella corrispondente costante {@code ActivityType}.
     * Il confronto viene effettuato ignorando le maiuscole/minuscole.
     * @param description La stringa che rappresenta il tipo di attività (es. "semina", "irrigazione", "raccolta").
     * @return Il valore {@code ActivityType} corrispondente alla stringa fornita.
     * @throws IllegalArgumentException Se la stringa fornita non corrisponde a nessun tipo di attività conosciuto.
     */
    public static ActivityType fromString(String description) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getDescription().equalsIgnoreCase(description)) {
                return activityType;
            }
        }
        throw new IllegalArgumentException("Tipo attività sconosciuto: " + description);
    }
}