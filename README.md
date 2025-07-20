# BioGarden - Documentazione Tecnica

Benvenuto nella documentazione tecnica di **BioGarden**! Questo README fornisce una panoramica approfondita dell'architettura e del funzionamento interno dell'applicazione. BioGarden è un sistema progettato per la gestione di progetti agricoli, colture, lotti, attività e utenti, con un'interfaccia utente basata su JavaFX.

---

## Architettura del Sistema

BioGarden adotta un'architettura **Layered Architecture** (architettura a strati), che separa le responsabilità in moduli distinti per migliorare la manutenibilità, la scalabilità e la testabilità. I principali strati includono:

1.  **Presentazione (UI / Controller):** Gestisce l'interfaccia utente e l'interazione con l'utente.
2.  **Servizio (Service / Business Logic):** Contiene la logica di business principale dell'applicazione.
3.  **Accesso ai Dati (DAO - Data Access Object):** Fornisce un'interfaccia astratta per l'interazione con il database.
4.  **Dominio (Models / DTOs / Enums):** Rappresenta le entità di business e i dati trasferiti tra gli strati.

Questo design è supportato dal pattern **Entity-Control-Boundary (ECB)**, che categorizza le classi in base al loro ruolo:

* **Boundary (Confine):** Classi che interagiscono con l'esterno del sistema (interfaccia utente, API esterne). Nel nostro caso, sono principalmente i **controller JavaFX** e la classe `BioGarden` principale.
* **Control (Controllo):** Classi che incapsulano la logica di business e coordinano le interazioni tra Boundary ed Entity. Questi includono i **Service Layer** e i **DAO (Data Access Object)**. Anche la classe `Session` e le utility (`Utils`) rientrano qui per la loro funzione di controllo e orchestrazione.
* **Entity (Entità):** Classi che rappresentano i dati e la logica di business fondamentale. Qui troviamo i **Model** (le classi di dominio che rappresentano gli oggetti di business come `Project`, `Colture`, `Farmer`) e i **DTO (Data Transfer Object)**, che sono versioni leggere dei modelli usate per il trasferimento dati tra gli strati. Anche le **enumerazioni** e le **eccezioni** sono considerate Entity in quanto definiscono aspetti del dominio.

---

## Componenti Chiave

### 1. Modelli (`com.unina.biogarden.models`)

Questa sezione definisce le entità principali del dominio dell'applicazione. Sono classi che rappresentano i dati e le loro relazioni,e espongono ai controller i dati essenziali per la visualizzazione grafica come:

* **`Activity` (e sottoclassi: `HarvestingActivity`, `IrrigationActivity`, `SeedingActivity`):** Rappresentano le diverse operazioni che possono essere eseguite su una coltura.
* **`Colture`:** Dettagli su una specifica coltivazione.
* **`Crop`:** Informazioni sui tipi di colture (es. pomodoro, insalata).
* **`Farmer`:** Rappresenta un utente di tipo "contadino".
* **`Lot`:** Descrive un appezzamento di terreno.
* **`Project`:** Un progetto agricolo che può contenere più colture.
* **`HarvestReportEntry` (in `com.unina.biogarden.models.report`):** Un modello specifico per i report di raccolta.

### 2. DTOs (`com.unina.biogarden.dto`)

I **Data Transfer Objects** sono classi leggere utilizzate per trasferire dati tra i diversi strati dell'applicazione. Spesso corrispondono ai modelli, ma possono essere ottimizzati per scopi specifici di trasferimento dati (es. includere solo i campi necessari per una determinata operazione).
Esempi includono `UserDTO`, `ProjectDTO`, `LotDTO`, `CropDTO`, `ColtureDTO` e le varie `ActivityDTO`.

### 3. DAOs (`com.unina.biogarden.dao`)

Il layer **DAO** è responsabile dell'interazione diretta con il database. Ogni DAO incapsula la logica per le operazioni CRUD (Create, Read, Update, Delete) relative a una specifica entità. Questo strato astrae i dettagli del database dal resto dell'applicazione.
Esempi: `UserDAO`, `ProjectDAO`, `LotDAO`, `CropDAO`, `ColtureDAO`, `ActivityDAO`.

### 4. Servizi (`com.unina.biogarden.service`)

Il **Service Layer** contiene la logica di business dell'applicazione. Coordina le operazioni che possono coinvolgere più DAO e impone le regole di business. Ogni servizio offre un'API di alto livello per le funzionalità dell'applicazione.

* **`AbstractService<T>`:** Fornisce un'interfaccia comune per i servizi, definendo operazioni come `insert` e `fetchAll`.
* **`ProjectService`:** Gestisce tutta la logica relativa a progetti, lotti, colture e attività. Interagisce con diversi DAO (`ProjectDAO`, `LotDAO`, `CropDAO`, `ColtureDAO`, `ActivityDAO`) per eseguire operazioni complesse, come l'aggiunta di un'attività a una coltura o la generazione di report.
* **`UserService`:** Gestisce la logica legata agli utenti, inclusi login, registrazione e recupero delle informazioni degli utenti.

### 5. Sessione (`com.unina.biogarden.session`)

La classe `Session` implementa il pattern **Singleton** e gestisce lo stato dell'utente attualmente loggato. Fornisce metodi statici per `login`, `logout`, `getUtente` e `isAuthenticated`, garantendo che ci sia un unico utente attivo alla volta.

### 6. Controller (`com.unina.biogarden.controller`)

Questo strato gestisce l'interfaccia utente (UI) dell'applicazione JavaFX. I controller rispondono agli eventi dell'utente, interagiscono con il Service Layer per eseguire operazioni di business e aggiornano la vista.
Esempi: `MainController`, `LoginController`, `RegistrationController`.

### 7. Utility (`com.unina.biogarden.utils`)

La classe `Utils` contiene metodi di utilità statici riutilizzabili in tutta l'applicazione. Questi includono:

* **Crittografia e Verifica Password:** Metodi per criptare e verificare le password utilizzando PBKDF2WithHmacSHA1, garantendo la sicurezza delle credenziali.
* **Gestione Scena JavaFX:** Metodi per impostare scene JavaFX e applicare fogli di stile.
* **Alert:** Funzioni per mostrare messaggi di avviso all'utente.
* **Formattazione Stringhe:** Metodi per la manipolazione di stringhe (es. capitalizzazione della prima lettera).

### 8. Eccezioni (`com.unina.biogarden.exceptions`)

Questo package contiene classi di eccezione personalizzate per gestire scenari specifici dell'applicazione, come `LoginFallitoException` o `UtenteEsistenteException`.

### 9. Enumerazioni (`com.unina.biogarden.enumerations`)

Definiscono set predefiniti di valori per proprietà specifiche, migliorando la leggibilità e riducendo gli errori. Esempi includono `ActivityStatus`, `ActivityType`, `ColtureStatus`, `UserType`.

---

## Flusso di Esempio: Login Utente

Per comprendere meglio il funzionamento, vediamo il flusso di un'operazione comune: il login dell'utente.

1.  **Interfaccia Utente (Boundary - `LoginController`):** L'utente inserisce email e password nell'interfaccia di login. Quando il pulsante di login viene cliccato, il `LoginController` cattura questi input.
2.  **Richiesta al Servizio (Control - `LoginController` a `UserService`):** Il `LoginController` chiama il metodo `login(email, password)` su un'istanza di `UserService`.
3.  **Logica di Business (Control - `UserService`):**
    * Il `UserService` riceve le credenziali.
    * Chiama il metodo `loginUser(email, password)` del `UserDAO` per verificare le credenziali nel database.
    * Se il login ha successo, il `UserDAO` restituisce un `UserDTO`.
    * Il `UserService` aggiorna la cache degli utenti e poi chiama `Session.login(UserDTO)` per impostare l'utente corrente nella sessione globale.
    * Se il login fallisce (es. credenziali non valide), `UserDAO` lancerà una `LoginFallitoException` che verrà propagata dal `UserService`.
4.  **Interazione con il Database (Control - `UserDAO`):** Il `UserDAO` esegue la query SQL necessaria per autenticare l'utente e recupera i suoi dati dal database. Utilizza le password criptate per la verifica.
5.  **Aggiornamento Sessione (Control - `Session`):** La classe `Session` memorizza il `UserDTO` dell'utente loggato, rendendolo disponibile a livello globale per il resto dell'applicazione.
6.  **Risposta alla UI (Boundary - `LoginController`):**
    * Se il login ha successo, il `LoginController` naviga l'utente alla schermata principale (es. `MainController`).
    * Se un'eccezione (`LoginFallitoException`) viene catturata, il `LoginController` visualizza un messaggio di errore appropriato all'utente utilizzando `Utils.showAlert()`.

---
## Diagramma delle classi
Di seguito il diagramma ECB del progetto BioGarden:

<img width="1261" height="1859" alt="ECB-Diagram" src="https://github.com/user-attachments/assets/3d5e27cf-c573-4753-9751-68132c922b1c" />
