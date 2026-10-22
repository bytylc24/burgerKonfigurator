# Anleitung Burger-Konfigurator fuer 2 Personen

Ziel: JavaFX-Anwendung, GUI ist vorgegeben (FXML + Scene Builder), ihr schreibt
die Java-Logik dahinter. Claude Code bekommt den technischen Auftrag
(CLAUDE_CODE_BRIEF.md) und baut das Grundgeruest, ihr versteht und erweitert es.

Warum nicht Konsole/exe: Die Aufgabe verlangt explizit eine Oberflaechenanwendung.
Eine Konsolen-exe erfuellt das nicht und ihr wuerdet bei der Abgabe Punkte verlieren.
JavaFX mit fertiger FXML ist nicht schwerer, ihr muesst nur Buttons/Felder mit Logik
verbinden statt die UI selbst zu bauen.

## Rollenaufteilung (Vorschlag)

**Person A - Model & Logik**
- Burger-Klasse (immutable)
- BurgerBuilder
- BurgerService (Geschaeftslogik)
- Validierung

**Person B - Persistenz & UI-Anbindung**
- Repository (JSON speichern/laden)
- Controller (verbindet FXML mit Service)
- Fehlerbehandlung in der UI (Alerts)

Ihr arbeitet trotzdem am selben Code, nicht isoliert. Abstimmen, bevor jemand
am Model etwas aendert, weil der andere darauf aufbaut.

## Schritt 1: Repository und Team

1. Einer von euch erstellt auf GitHub ein neues Repository, z.B. `burger-konfigurator`
2. Sichtbarkeit: Private reicht, Public auch ok
3. Mit README und `.gitignore` fuer Java (Vorlage "Maven" auswaehlen beim Erstellen)
4. Der zweite wird als Collaborator eingeladen: Settings -> Collaborators -> Add people

## Schritt 2: Projekt lokal aufsetzen

Beide:
1. Repository klonen: `git clone <repo-url>`
2. IntelliJ oeffnen, Projekt als Maven-Projekt importieren
3. Java 17 oder neuer als Project SDK einstellen (File -> Project Structure)

## Schritt 3: FXML mit Scene Builder bauen

Ihr braucht Scene Builder (kostenlos, von Gluon): https://gluonhq.com/products/scene-builder/

Zwei Dateien erstellen unter `src/main/resources/<euer-package>/`:

1. `configurator-view.fxml`
   - ComboBox oder ToggleGroup fuer: Brot, Patty, Kaese, Sauce
   - CheckBoxen fuer Gemuese (z.B. Tomate, Salat, Zwiebel)
   - CheckBoxen fuer Extras (z.B. Bacon, Ei)
   - Button "Burger erstellen"
   - Wichtig: jedem Element eine fx:id geben (rechts im Scene Builder unter "Code"),
     sonst kann der Controller nicht darauf zugreifen

2. `overview-view.fxml`
   - ListView oder TableView fuer die Liste der erstellten Burger
   - Button "Bearbeiten"
   - Button "Loeschen"

Tipp: Scene Builder kann man auch direkt in IntelliJ einbinden (Settings ->
Languages & Frameworks -> JavaFX), dann oeffnet ein Doppelklick auf die .fxml
direkt den Scene Builder.

Wenn die FXML stehen: committen und pushen, bevor ihr Claude Code startet.
Claude Code baut dann die Controller passend zu euren fx:id-Namen.

## Schritt 4: Claude Code arbeiten lassen

1. Terminal in IntelliJ oeffnen (Alt+F12) oder normales Terminal im Projektordner
2. Falls noch nicht installiert: Claude Code installieren (siehe offizielle Anleitung,
   kurz: `npm install -g @anthropic-ai/claude-code`, Node.js muss installiert sein)
3. Im Projektordner: `claude` eingeben zum Starten
4. Den Inhalt von `CLAUDE_CODE_BRIEF.md` einfuegen bzw. die Datei referenzieren,
   z.B.: "Lies CLAUDE_CODE_BRIEF.md und setze das Projekt danach um. Die FXML
   Dateien liegen schon in src/main/resources, bitte nicht veraendern."
5. Claude Code lässt die Struktur, Klassen und `pom.xml` bauen

## Schritt 5: Code verstehen (nicht nur abnicken)

Bevor ihr committet, beide einmal durchlesen:

- Warum hat `Burger` keine Setter? -> Immutable Pattern, im Brief erklaert
- Wie funktioniert der `BurgerBuilder`? -> Methode fuer Methode nachvollziehen
- Wo wird gespeichert/geladen? -> Repository-Klasse anschauen
- Was passiert bei einer ungueltigen Eingabe? -> Validierung im Service nachlesen

Wenn etwas unklar ist: fragt in Claude Code direkt nach ("Erklaer mir, warum
BurgerBuilder.build() eine neue Instanz zurueckgibt und nicht this veraendert").

## Schritt 6: Testen

1. `mvn javafx:run` im Terminal, oder in IntelliJ die Main-Klasse starten
2. Burger erstellen, Anwendung schliessen, neu starten, pruefen ob der Burger
   noch da ist (Persistenz-Test)
3. Ungueltige Eingabe testen (z.B. kein Patty ausgewaehlt) -> muss Fehlermeldung
   zeigen, nicht abstuerzen
4. Burger "bearbeiten" -> pruefen ob wirklich ein neuer Burger entsteht und der
   alte noch in der Liste bleibt (das ist der Kern vom Immutable Pattern)

## Schritt 7: Git-Workflow waehrend der Arbeit

Nicht direkt auf `main` pushen, sondern:

1. Branch erstellen: `git checkout -b feature/model` bzw. `feature/ui`
2. Aenderungen committen: `git add .` dann `git commit -m "kurze Beschreibung"`
3. Pushen: `git push origin feature/model`
4. Auf GitHub einen Pull Request erstellen
5. Der andere schaut kurz drueber und mergt (oder kommentiert)

Das ist auch fuer die Doku gut, weil man im Nachhinein sieht wer was gemacht hat.

## Schritt 8: Dokumentation

Wird nicht von Claude Code geschrieben, das macht ihr selbst, siehe Angabe:

- Kurze Beschreibung der Anwendung
- Beschreibung des Immutable Patterns
- Begruendung, warum Immutable fuer dieses Projekt passt (Stichwort: "Aendern
  bedeutet neues Objekt erzeugen, nicht das alte veraendern")
- UML-Klassendiagramm (z.B. mit draw.io oder PlantUML)
- Kurzbeschreibung der wichtigsten Klassen (Burger, BurgerBuilder, BurgerService,
  BurgerRepository)
- Beschreibung der Datenspeicherung (JSON-Datei, Aufbau)

## Checkliste vor der Abgabe

- [ ] Burger-Objekt hat keine Setter, alle Felder final
- [ ] Bearbeiten erzeugt neues Objekt, altes bleibt bestehen
- [ ] Daten bleiben nach Neustart erhalten
- [ ] Ungueltige Eingaben fuehren zu Fehlermeldung, nicht zu Absturz
- [ ] Mindestens 2 Ansichten vorhanden (Konfigurator + Uebersicht)
- [ ] Code sauber in model/repository/service/controller getrennt
- [ ] README im Repo beschreibt kurz was das Projekt macht und wie man es startet
- [ ] Dokumentation fertig (siehe Schritt 8)
