package ca.udem.maville.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public final class DateManagement {

    // Passe d'une date au format ISO à une date de type dd/mm/yyyy
    public static String formatIsoDate(String isoDate) {

        // Étape 1 : Parser la chaîne ISO en Instant (UTC)
        Instant instant = Instant.parse(isoDate);

        // Étape 2 : Convertir en date locale si besoin
        // Ici je prends l'heure de Montréal comme exemple
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("America/Toronto"));

        // Étape 3 : Formater au format "dd/MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormatee = dateTime.format(formatter);

        return dateFormatee;
    }

    // Passe d'une date au format dd/mm/yyyy à un format ISO
    public static String formatDateFR(String dateFR) {
        // Définir le format d'entrée
        DateTimeFormatter formatterEntree = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Parser la date en LocalDate
        LocalDate date = LocalDate.parse(dateFR, formatterEntree);

        // Convertir en ZonedDateTime à minuit UTC
        ZonedDateTime dateTimeUTC = date.atStartOfDay(ZoneOffset.UTC);

        // Retourner au format ISO
        return dateTimeUTC.toString(); // ISO 8601
    }
}
