package com.example.books;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Diese Klasse dient dem Austausch von Daten mit einem Server.
 * Sie lädt Datensätze und sendet die Verarbeitungsergebnisse zurück.
 */
public class DatenAustausch {

    // Basis-URL für die API
    private final String BASIS_URL = "http://localhost:8080/v1/datensatz";

    // URL-Erweiterungen für verschiedene Endpunkte
    public enum UrlErweiterungen {
        DATENSATZ("/v1/datensatz"),
        ERGEBNIS("/v1/ergebnis"),
        GESUNDHEIT("/gesundheit");

        public final String pfad;

        UrlErweiterungen(String erweiterung) {
            this.pfad = erweiterung;
        }
    }

    /**
     * Ruft den Datensatz als JSON von der API ab.
     * @return Der JSON-String, der den Datensatz darstellt.
     */
    public String holeDatensatzAlsJson() {
        try {
            // Erstelle URL-Objekt aus BASIS_URL
            URL url = new URI(BASIS_URL).toURL();

            // Öffne einen Stream zum Lesen der Daten
            try (InputStream inputStream = url.openStream();
                 BufferedReader leser = new BufferedReader(new InputStreamReader(inputStream))) {

                // Lese Daten zeilenweise und füge sie zu einem StringBuilder hinzu
                StringBuilder ergebnis = new StringBuilder();
                String zeile;
                while ((zeile = leser.readLine()) != null) {
                    ergebnis.append(zeile);
                }
                // Rückgabe des gesamten JSON-Inhalts
                return ergebnis.toString();
            } catch (IOException e) {
                throw new RuntimeException("Fehler beim Lesen der Daten von der URL: " + BASIS_URL, e);
            }
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException("Fehlerhafte URL/URI: " + BASIS_URL, e);
        }
    }

    /**
     * Sendet die Verarbeitungsresultate an den Server.
     * @param json Der JSON-String, der das Resultat enthält.
     */
    public void sendeErgebnis(String json) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest anfrage = HttpRequest.newBuilder()
                // Erstelle POST-Anfrage an die API
                .uri(URI.create(erstelleUrl(UrlErweiterungen.ERGEBNIS)))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> antwort = client.send(anfrage, HttpResponse.BodyHandlers.ofString());
            // Überprüfe, ob der Statuscode erfolgreich ist
            if (antwort.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Fehler beim Senden des Ergebnisses: " + antwort.body());
            }
            System.out.println("Ergebnis erfolgreich gesendet: " + antwort.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HTTP Fehler: " + e.getMessage());
        }
    }

    // Hilfsmethode zur Erstellung der URL basierend auf der UrlErweiterung
    private String erstelleUrl(UrlErweiterungen erweiterung) {
        return BASIS_URL + erweiterung.pfad;
    }
}
