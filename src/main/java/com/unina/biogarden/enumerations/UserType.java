package com.unina.biogarden.enumerations;

/**
 * Enumerazione che rappresenta i diversi tipi di utente nel sistema BioGarden.
 * Ogni tipo di utente è associato a una descrizione testuale interna,
 * utile per la logica dell'applicazione e la persistenza nel database.
 * @author Il Tuo Nome
 */
public enum UserType {
    /**
     * Rappresenta un utente proprietario, tipicamente colui che gestisce i lotti.
     */
    OWNER("proprietario"),
    /**
     * Rappresenta un utente coltivatore, tipicamente colui che esegue le attività sul campo.
     */
    FARMER("coltivatore");

    private final String type;

    /**
     * Costruttore per {@code UserType}.
     * @param type Il valore stringa interno che rappresenta il tipo di utente.
     */
    UserType(String type) {
        this.type = type;
    }

    /**
     * Restituisce il valore stringa interno del tipo di utente.
     * @return Il tipo di utente come stringa.
     */
    public String getType() {
        return type;
    }

    /**
     * Converte una stringa nel corrispondente valore {@code UserType}.
     * Il confronto viene effettuato ignorando le maiuscole/minuscole.
     * @param type La stringa che rappresenta il tipo di utente (es. "proprietario", "coltivatore").
     * @return L'enum {@code UserType} corrispondente alla stringa fornita.
     * @throws IllegalArgumentException Se la stringa fornita non corrisponde a nessun tipo di utente conosciuto.
     */
    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.getType().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Tipo utente sconosciuto: " + type);
    }
}