package com.unina.biogarden.enumerations;

/**
 * Enumerazione che rappresenta i possibili stati di un'attività nel sistema BioGarden.
 * Ogni stato ha un valore interno (utilizzato tipicamente per la persistenza o la logica interna)
 * e un'etichetta descrittiva per la visualizzazione all'utente.
 * @author Il Tuo Nome
 */
public enum ActivityStatus {
    /**
     * L'attività è stata pianificata ma non è ancora iniziata.
     */
    PLANNED("pianificata", "Pianificata"),
    /**
     * L'attività è attualmente in corso di esecuzione.
     */
    IN_PROGRESS("in_corso", "In Corso"),
    /**
     * L'attività è stata completata con successo.
     */
    COMPLETED("terminata", "Completata");

    private final String status, label;

    /**
     * Costruttore per {@code ActivityStatus}.
     * @param status Il valore stringa interno dello stato (es. "pianificata").
     * @param label L'etichetta descrittiva dello stato per l'utente (es. "Pianificata").
     */
    ActivityStatus(String status, String label) {
        this.status = status;
        this.label = label;
    }

    /**
     * Restituisce l'etichetta descrittiva dello stato dell'attività.
     * Questa è la stringa destinata alla visualizzazione nell'interfaccia utente.
     * @return L'etichetta dello stato.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Restituisce il valore stringa interno dello stato dell'attività.
     * Questo valore è utile per l'interazione con il database o con logiche interne.
     * @return Il valore interno dello stato.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Converte una stringa nel corrispondente valore {@code ActivityStatus}.
     * Il confronto avviene ignorando le maiuscole/minuscole.
     * @param status La stringa che rappresenta lo stato dell'attività (es. "pianificata", "in_corso", "terminata").
     * @return L'enum {@code ActivityStatus} corrispondente alla stringa fornita.
     * @throws IllegalArgumentException Se la stringa fornita non corrisponde a nessuno stato di attività conosciuto.
     */
    public static ActivityStatus fromString(String status) {
        for (ActivityStatus activityStatus : ActivityStatus.values()) {
            if (activityStatus.getStatus().equalsIgnoreCase(status)) {
                return activityStatus;
            }
        }
        throw new IllegalArgumentException("Stato attività sconosciuto: " + status);
    }
}