# Projektauftrag für Claude Code: Burger-Konfigurator (Projekt 11)

## Kontext

Schulprojekt HTL Kaindorf, 3. Klasse Informatik, Design-Patterns-Projektaufgabe.
Team: 2 Personen. Sprache: Java mit JavaFX. Build-Tool: Maven.

## Aufgabenstellung (Original, gekürzt)

Eine Anwendung, mit der Benutzer einen individuellen Burger konfigurieren.
Nach dem Erstellen ist ein fertiger Burger unveränderlich (Immutable Pattern).
Jede Änderung erzeugt ein neues Burger-Objekt, das alte bleibt unverändert bestehen.

### Muss-Anforderungen

- Oberflächenanwendung (JavaFX)
- Objektorientierte Programmierung
- Persistente Speicherung (JSON empfohlen, z.B. mit Gson oder Jackson)
- Daten bleiben nach Neustart erhalten
- Trennung von Oberfläche (FXML/Controller), Logik (Model/Service) und Datenhaltung (Repository/Storage)
- Pflicht-Pattern: Immutable
- Burger kann enthalten: Brot, Patty, Käse, Sauce, Gemüse, Extras
- Nach dem Erstellen (build) darf das Burger-Objekt NICHT mehr verändert werden
- Eine "Änderung" an einem bestehenden Burger erzeugt ein komplett neues Burger-Objekt, das alte bleibt im Bestand erhalten
- Validierung von Benutzereingaben (z.B. mindestens ein Patty, kein leerer Name)
- Fehlerbehandlung (z.B. fehlerhafte/fehlende Daten beim Laden, ungültige Eingaben)
- Übersichtliche, benutzerfreundliche Oberfläche

### Kann-Anforderungen (optional, nach Zeit)

- Nährwertberechnung
- Preisberechnung
- Favoriten
- Bestellhistorie
- Vordefinierte Burger (Presets)

### Datenmodell (Vorgabe aus Angabe)

```
Burger
----------------
id
brot
patty
kaese
sauce
zutaten (Liste Gemüse)
extras (Liste)
preis
```

## Architektur-Vorgabe

Schichtenarchitektur, strikt getrennt:

```
src/main/java/<package>/
├── model/          -> Burger (immutable), BurgerBuilder, Enums (Brot, Patty, Kaese, Sauce, ...)
├── repository/      -> BurgerRepository (Interface) + JsonBurgerRepository (Implementierung)
├── service/         -> BurgerService (Geschäftslogik, nutzt Repository)
├── controller/       -> JavaFX-Controller (ein Controller pro FXML-View)
└── Main.java         -> JavaFX Application-Einstiegspunkt

src/main/resources/<package>/
└── *.fxml            -> UI-Layouts (werden separat bereitgestellt, siehe unten)
```

### Wichtig zum Immutable Pattern

- Die `Burger`-Klasse hat NUR finale Felder (`private final ...`), keine Setter
- Eine neue Instanz entsteht ausschließlich über einen Builder (`BurgerBuilder`)
- "Burger bearbeiten" in der UI bedeutet: alte Werte in den Builder vorbefüllen, Änderungen vornehmen, `build()` aufrufen -> neue ID, neues Objekt. Der alte Burger bleibt im Repository bestehen (oder wird bewusst ersetzt, aber nie mutiert)
- Falls Listen (Gemüse, Extras) im Konstruktor übergeben werden: intern als unveränderliche Kopie speichern (z.B. `List.copyOf(...)`), damit niemand von außen die Liste nachträglich verändern kann

### FXML-Vorgabe

Die FXML-Dateien werden von uns (dem Team) vorgegeben und liegen bereits in
`src/main/resources/<package>/`. Bitte NICHT das Layout/die fx:id-Namen in den
FXML-Dateien verändern, sondern die Controller darauf aufbauen. Falls eine
fx:id in der FXML fehlt, die für die Logik gebraucht wird, bitte explizit
nachfragen bzw. kommentieren statt die FXML eigenmächtig umzubauen.

Erwartete Views (Minimum):

1. `configurator-view.fxml` - Burger zusammenstellen (ComboBoxen/ToggleGroups für Brot, Patty, Käse, Sauce, CheckBoxen für Gemüse/Extras, Button "Burger erstellen")
2. `overview-view.fxml` - Liste aller erstellten Burger (ListView oder TableView), Button "Bearbeiten" (erzeugt neuen Burger auf Basis des alten), Button "Löschen"

## Persistenz

- JSON-Datei im Projektverzeichnis (z.B. `burgers.json`)
- Bibliothek: Gson (einfacher für Schulprojekt) via Maven-Dependency
- Repository-Interface so gestalten, dass Speichertechnologie austauschbar wäre (auch wenn nur eine Implementierung existiert)

## Validierung & Fehlerbehandlung

- Burger ohne Patty oder ohne Brot darf nicht erstellt werden -> Fehlermeldung in der UI (z.B. Alert-Dialog), keine Exception die das Programm crasht
- Beim Laden der JSON-Datei: falls Datei fehlt oder korrupt ist, Anwendung darf nicht abstürzen, sondern soll mit leerer Liste starten und eine verständliche Meldung/Log ausgeben

## Nicht tun

- Keine Setter in der Burger-Klasse
- Keine globalen/statischen Mutable-States als Workaround
- FXML-Struktur nicht eigenmächtig neu designen, nur Controller-Logik ergänzen
- Keine Business-Logik im Controller, die eigentlich in den Service gehört (Controller ruft Service auf, Service entscheidet)

## Technischer Rahmen

- Java 17+, Maven, JavaFX (via `org.openjfx` Maven-Plugin)
- Gson für JSON
- Namen und Kommentare im Code auf Englisch, Kommentare kurz halten
- `pom.xml` bitte lauffähig konfigurieren inkl. JavaFX-Plugin, sodass `mvn javafx:run` funktioniert

## Dokumentation (separat zu erstellen, nicht Teil des Codes)

Wird vom Team selbst geschrieben, nicht von Claude Code. Enthält: Kurzbeschreibung,
Pattern-Begründung, UML-Klassendiagramm, Klassenbeschreibung, Datenspeicherung.
