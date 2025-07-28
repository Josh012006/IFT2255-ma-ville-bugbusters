package ca.udem.maville.utils;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import java.time.Instant;
import java.util.Date;
import java.util.Random;



/**
 * Une fontion utilitaire qui regroupe des fonctions qui sont utilisées dans les controllers.
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


    public static boolean isSimilar(String text1, String text2, double threshold) throws IOException {
        Map<String, Integer> freq1 = getTermFrequencies(text1);
        Map<String, Integer> freq2 = getTermFrequencies(text2);

        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(freq1.keySet());
        allTerms.addAll(freq2.keySet());

        List<Double> vec1 = new ArrayList<>();
        List<Double> vec2 = new ArrayList<>();

        for (String term : allTerms) {
            vec1.add((double) freq1.getOrDefault(term, 0));
            vec2.add((double) freq2.getOrDefault(term, 0));
        }

        double similarity = cosineSimilarity(vec1, vec2);
        return similarity >= threshold;
    }

    private static Map<String, Integer> getTermFrequencies(String text) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        Analyzer analyzer = new StandardAnalyzer();
        try (var tokenStream = analyzer.tokenStream(null, new StringReader(text))) {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = tokenStream.getAttribute(CharTermAttribute.class).toString();
                frequencies.put(term, frequencies.getOrDefault(term, 0) + 1);
            }
            tokenStream.end();
        }
        return frequencies;
    }

    private static double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            dot += vec1.get(i) * vec2.get(i);
            norm1 += Math.pow(vec1.get(i), 2);
            norm2 += Math.pow(vec2.get(i), 2);
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10); // évite la division par 0
    }

}
