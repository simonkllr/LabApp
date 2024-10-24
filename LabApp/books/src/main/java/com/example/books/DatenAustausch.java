package com.example.books;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DatenAustausch {
    
    private final String BASIS_URL = "http://assessment:8080/v1/dataset";

   // Holt den Datensatz als JSON
public String holeDatensatzAlsJson() {
    try {
        // Erstelle URL-Objekt basierend auf der BASIS_URL
        URL url = new URI(BASIS_URL).toURL();
        
        // Öffne einen Stream, um die Daten von der URL zu lesen
        try (InputStream inputStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            // Verwende StringBuilder für bessere Performance beim Zusammenfügen von Strings
            StringBuilder result = new StringBuilder();
            String line;
            
            // Lies jede Zeile und füge sie dem StringBuilder hinzu
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            
            return result.toString();  // Gib das vollständige JSON zurück
            
        } catch (IOException e) {
            // Fehler beim Öffnen/Lesen der Verbindung
            throw new RuntimeException("Fehler beim Lesen der Daten von der URL: " + BASIS_URL, e);
        }
        
    } catch (URISyntaxException | MalformedURLException e) {
        // Fehlerhafte URL oder URI-Syntax
        throw new RuntimeException("Fehlerhafte URL/URI: " + BASIS_URL, e);
    }
}


    // Sendet das Ergebnis per POST an den Server
    public void sendeErgebnis(String json) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(erstelleWebsiteUrl(UrlExtensions.RESULT)))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Fehler beim Senden des Ergebnisses: " + response.body());
            }
            System.out.println("Ergebnis erfolgreich gesendet: " + response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("HTTP Fehler: " + e.getMessage());
        }
    }

    // Hilfsmethode zur Erstellung der URL
    private String erstelleWebsiteUrl(UrlExtensions extension) {
        return BASIS_URL + extension.path;
    }
}
