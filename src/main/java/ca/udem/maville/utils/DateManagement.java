package ca.udem.maville.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateManagement {

    // Passe d'une date au format ISO à une date de type dd/mm/yyyy
    public static String formatIsoDate(String isoDate) {

        // Étape 1 : Parser la chaîne ISO en Instant (UTC)
        Instant instant = Instant.parse(isoDate);

        // Étape 2 : Convertir en date locale si besoin
        // Ici je prends l'heure de Montréal comme exemple
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("America/Toronto"));

        // Étape 3 : Formater au format "dd/MM/yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dateFormatee = dateTime.format(formatter);

        return dateFormatee;
    }

    // Passe d'une date au format dd/mm/yyyy à un format ISO
    public static String formatDateFR(String dateFR) {
        // 1. Définir le format d'entrée
        DateTimeFormatter formatterEntree = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 2. Parser la date en LocalDate
        LocalDate date = LocalDate.parse(dateFR, formatterEntree);

        // 3. Convertir en ZonedDateTime à 01:00 UTC
        ZonedDateTime dateTimeUTC = date.atStartOfDay(ZoneOffset.UTC).plusHours(1);

        // 4. Formatter au format ISO avec heures, minutes et secondes
        DateTimeFormatter formatterSortie = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

        return dateTimeUTC.format(formatterSortie);
    }

    // Permet de former un objet Date adéquat
    public static Date getDateIso(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));

        return Date.from(zonedDateTime.toInstant());
    }
}
