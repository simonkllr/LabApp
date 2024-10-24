package com.example.books;


public class Main {
    public static void main(String[] args) {
        DatenAustausch datenAustausch = new DatenAustausch();
        DatenVerarbeitung datenVerarbeitung = new DatenVerarbeitung();

        // Daten von der API abrufen
        String datasetJson = datenAustausch.holeDatensatzAlsJson();

        // JSON zu Dataset konvertieren
        Dataset dataset = datenVerarbeitung.konvertiereJsonZuDatensatz(datasetJson);

        // Daten verarbeiten (Berechnung der Laufzeiten)
        Result result = datenVerarbeitung.verarbeiteDatensatzZuErgebnis(dataset);

        // Resultat als JSON konvertieren
        String resultJson = datenVerarbeitung.konvertiereErgebnisZuJson(result);

        // Ergebnis an API senden
        datenAustausch.sendeErgebnis(resultJson);
    }
}
