package com.example.books;

/**
 * Die Hauptklasse steuert den gesamten Ablauf des Programms.
 * Sie ruft den Datensatz ab, verarbeitet ihn und sendet die Ergebnisse zurück.
 */
public class Main {
    public static void main(String[] args) {
        DatenAustausch datenAustausch = new DatenAustausch();
        DatenVerarbeitung datenVerarbeitung = new DatenVerarbeitung();

        // 1. Daten von der API abrufen
        String datensatzJson = datenAustausch.holeDatensatzAlsJson();

        // 2. JSON zu Datensatz konvertieren
        DatenVerarbeitung.Datensatz datensatz = datenVerarbeitung.konvertiereJsonZuDatensatz(datensatzJson);

        // 3. Daten verarbeiten (Berechnung der Laufzeiten)
        DatenVerarbeitung.Ergebnis ergebnis = datenVerarbeitung.verarbeiteDatensatzZuErgebnis(datensatz);

        // 4. Ergebnis als JSON konvertieren
        String ergebnisJson = datenVerarbeitung.konvertiereErgebnisZuJson(ergebnis);

        // 5. Ergebnis an API senden
        datenAustausch.sendeErgebnis(ergebnisJson);
    }
}
