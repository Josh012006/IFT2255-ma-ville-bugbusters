package ca.udem.maville.utils;


import java.time.Instant;
import java.util.Date;
import java.util.Random;

/**
 * Une fontion utilitaire qui regroupe de sfonctions qui sont utilisées dans les controllers.
 */
public final class ControllerHelper {

    public static String getTypeTravail(String reason, TypesTravaux[] typesTravaux) {
        if(reason.equals("Autre")) {
            // Si c'est Autre, en choisir un type de travail au hasard
            // avec une répartition uniforme
            int index = getRandomUniformInt(0, typesTravaux.length); // génère un entier entre 0 et array.length - 1
            return typesTravaux[index].getLabel();
        } else {
            // Sinon, en regardant les résultats de la requête à l'API, on remarque
            // que le reste est pour la plupart des travaux de construction ou rénovation
            return TypesTravaux.constructionOuRenovation.getLabel();
        }
    }

    public static int getRandomUniformInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(min, max);
    }

    public static double getRandomUniformDouble(double min, double max) {
        Random random = new Random();
        return random.nextDouble(min, max);
    }

    public static Date fromIsoString(String isoDate) {
        Instant instant = Instant.parse(isoDate);
        return Date.from(instant);
    }

}
