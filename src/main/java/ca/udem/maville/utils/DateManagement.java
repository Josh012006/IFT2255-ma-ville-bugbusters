package ca.udem.maville.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateManagement {

    public static String formatDate(String isoDate) {

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
}
