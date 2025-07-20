package com.unina.biogarden.enumerations;

/**
 * Enumerazione che rappresenta i possibili stati di una coltivazione nel sistema BioGarden.
 * Ogni stato è associato a un valore stringa interno, tipicamente utilizzato per la persistenza nel database
 * o per la logica di business.
 * @author Il Tuo Nome
 */
public enum ColtureStatus {
    /**
     * La coltivazione è in attesa di essere seminata o di iniziare il suo ciclo.
     */
    WAITING("attesa"),
    /**
     * La coltivazione è stata seminata.
     */
    SEEDED("seminato"),
    /**
     * La coltivazione è maturata ed è pronta per la raccolta.
     */
    GROWED("maturo"),
    /**
     * La coltivazione è stata raccolta.
     */
    HARVESTED("raccolto");

    private final String status;

    /**
     * Costruttore per {@code ColtureStatus}.
     * @param status Il valore stringa interno che rappresenta lo stato della coltivazione.
     */
    ColtureStatus(String status) {
        this.status = status;
    }

    /**
     * Restituisce il valore stringa interno dello stato della coltivazione.
     * @return Il valore stringa dello stato.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Converte una stringa nel corrispondente valore {@code ColtureStatus}.
     * Il confronto avviene ignorando le maiuscole/minuscole.
     * @param status La stringa che rappresenta lo stato della coltivazione (es. "attesa", "seminato").
     * @return L'enum {@code ColtureStatus} corrispondente alla stringa fornita.
     * @throws IllegalArgumentException Se la stringa fornita non corrisponde a nessuno stato di coltivazione conosciuto.
     */
    public static ColtureStatus fromString(String status) {
        for (ColtureStatus coltureStatus : ColtureStatus.values()) {
            if (coltureStatus.getStatus().equalsIgnoreCase(status)) {
                return coltureStatus;
            }
        }
        throw new IllegalArgumentException("Stato coltivazione sconosciuto: " + status);
    }
}