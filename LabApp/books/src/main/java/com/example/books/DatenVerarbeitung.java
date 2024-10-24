package com.example.books;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;

/**
 * Diese Klasse verarbeitet Datensätze, konvertiert JSON in Objekte
 * und berechnet die Gesamtnutzungszeit der Workloads pro Kunde.
 */
public class DatenVerarbeitung {

    /**
     * Diese Klasse repräsentiert einen Kunden mit einer bestimmten Nutzungszeit.
     */
    public static class Kunde {
        private String kundenId;
        private long nutzungszeit;

        public Kunde(String kundenId, long nutzungszeit) {
            this.kundenId = kundenId;
            this.nutzungszeit = nutzungszeit;
        }

        public String getKundenId() {
            return kundenId;
        }

        public long getNutzungszeit() {
            return nutzungszeit;
        }

        public void setNutzungszeit(long nutzungszeit) {
            this.nutzungszeit = nutzungszeit;
        }
    }

    /**
     * Diese Klasse repräsentiert ein Ereignis (Start/Stop) eines Workloads für einen Kunden.
     */
    public static class Ereignis {
        private String kundenId;
        private long zeitstempel;
        private String ereignisTyp;

        public String getKundenId() {
            return kundenId;
        }

        public long getZeitstempel() {
            return zeitstempel;
        }

        public String getEreignisTyp() {
            return ereignisTyp;
        }


    }

    /**
     * Diese Klasse repräsentiert einen Datensatz, der eine Liste von Ereignissen enthält.
     */
    public static class Datensatz {
        private List<Ereignis> ereignisse;

        public List<Ereignis> getEreignisse() {
            return ereignisse;
        }
    }

    /**
     * Diese Klasse enthält die berechneten Ergebnisse für mehrere Kunden.
     */
    public static class Ergebnis {
        private Collection<Kunde> ergebnisse;

        public Ergebnis(Collection<Kunde> ergebnisse) {
            this.ergebnisse = ergebnisse;
        }

        public Collection<Kunde> getErgebnisse() {
            return ergebnisse;
        }
    }

    /**
     * Konvertiert einen JSON-String zu einem Datensatz-Objekt.
     * @param json Der JSON-String, der den Datensatz repräsentiert.
     * @return Ein Datensatz-Objekt.
     */
    public Datensatz konvertiereJsonZuDatensatz(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Datensatz.class);
    }

    /**
     * Konvertiert ein Ergebnis-Objekt zu einem JSON-String.
     * @param ergebnis Das Ergebnis-Objekt, das die Ergebnisse enthält.
     * @return Ein JSON-String, der das Ergebnis darstellt.
     */
    public String konvertiereErgebnisZuJson(Ergebnis ergebnis) {
        Gson gson = new Gson();
        return gson.toJson(ergebnis);
    }

    /**
     * Verarbeitet den Datensatz und berechnet die Gesamtnutzungszeit für jeden Kunden.
     * @param datensatz Der Datensatz mit den Start- und Stop-Ereignissen.
     * @return Das berechnete Ergebnis für alle Kunden.
     */
    public Ergebnis verarbeiteDatensatzZuErgebnis(Datensatz datensatz) {
        HashMap<String, Kunde> kundenErgebnisse = new HashMap<>();

        // Iteriere über alle Ereignisse
        datensatz.getEreignisse().forEach(ereignis -> {
            // Wenn Kunde existiert, aktualisiere Nutzungszeit basierend auf dem Ereignis-Typ
            kundenErgebnisse.computeIfPresent(ereignis.getKundenId(), (id, kunde) -> {
                if (ereignis.getEreignisTyp().equals("start")) {
                    // Start-Ereignis: Nutzungszeit reduzieren
                    kunde.setNutzungszeit(kunde.getNutzungszeit() - ereignis.getZeitstempel());
                } else if (ereignis.getEreignisTyp().equals("stop")) {
                    // Stop-Ereignis: Nutzungszeit erhöhen
                    kunde.setNutzungszeit(kunde.getNutzungszeit() + ereignis.getZeitstempel());
                }
                return kunde;
            });

            // Falls Kunde nicht existiert, füge neuen Kunden hinzu
            kundenErgebnisse.putIfAbsent(ereignis.getKundenId(), new Kunde(ereignis.getKundenId(),
                    ereignis.getEreignisTyp().equals("start") ? -ereignis.getZeitstempel() : ereignis.getZeitstempel()));
        });

        // Rückgabe des Ergebnisses
        return new Ergebnis(kundenErgebnisse.values());
    }
}
