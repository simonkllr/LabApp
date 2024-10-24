package com.example.books;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class DatenVerarbeitung {

    // Konvertiert JSON zu Dataset-Objekt
    public Dataset konvertiereJsonZuDatensatz(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Dataset.class);
    }

    // Konvertiert Result-Objekt zu JSON
    public String konvertiereErgebnisZuJson(Result result) {
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    // Verarbeitet Dataset und berechnet die Gesamtlaufzeit pro Kunde
    public Result verarbeiteDatensatzZuErgebnis(Dataset dataset) {
        
        HashMap<String, Customer> kundenErgebnisse = new HashMap<>();

        dataset.events().forEach(event -> {
            kundenErgebnisse.computeIfPresent(event.customerId(), (id, kunde) -> {
                if (event.eventType().equals("start")) {
                    kunde.setConsumption(kunde.getConsumption() - event.timestamp());
                } else if (event.eventType().equals("stop")) {
                    kunde.setConsumption(kunde.getConsumption() + event.timestamp());
                }
                return kunde;
            });

            kundenErgebnisse.putIfAbsent(event.customerId(), new Customer(event.customerId(),
                    event.eventType().equals("start") ? -event.timestamp() : event.timestamp()));
        });

        return new Result(kundenErgebnisse.values());
    }
}
