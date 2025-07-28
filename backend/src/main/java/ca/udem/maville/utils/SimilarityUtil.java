package ca.udem.maville.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public final class SimilarityUtil {

    /** Stop‑words spécifiques à ton domaine */
    private static final Set<String> CUSTOM_STOP_WORDS = Set.of(
            // Mots de liaison / stop‑words fréquents
            "rue", "chemin", "quartier", "route", "voie", "avenue", "allée", "boulevard",
            "depuis", "durant", "pendant", "en", "le", "la", "les", "un", "une", "des",

            // Verbes / expressions trop fréquentes
            "être", "avoir", "faire", "possible", "impossible", "peux", "pouvoir",
            "merci", "svp", "s’il", "vous", "nous"
    );

    /** Nombre minimum de termes informatifs communs pour valider une similarité */
    private static final int MIN_COMMON_TERMS = 2;

    public static boolean isSimilar(String text1, String text2, double threshold) throws IOException {
        Map<String, Integer> freq1 = getTermFrequencies(text1);
        Map<String, Integer> freq2 = getTermFrequencies(text2);

        // Nombre de termes informatifs communs
        long commonTerms = freq1.keySet().stream()
                .filter(freq2::containsKey)
                .count();
        if (commonTerms < MIN_COMMON_TERMS) {
            return false;
        }

        // Construction des vecteurs pondérés (1 + log(tf))
        Set<String> allTerms = new HashSet<>();
        allTerms.addAll(freq1.keySet());
        allTerms.addAll(freq2.keySet());

        List<Double> vec1 = new ArrayList<>(allTerms.size());
        List<Double> vec2 = new ArrayList<>(allTerms.size());
        for (String term : allTerms) {
            vec1.add(weightedTf(freq1.getOrDefault(term, 0)));
            vec2.add(weightedTf(freq2.getOrDefault(term, 0)));
        }

        double similarity = cosineSimilarity(vec1, vec2);
        System.out.printf("similarity=%.3f, commonTerms=%d%n", similarity, commonTerms);
        return similarity >= threshold;
    }

    /** Transforme une fréquence brute en poids TF : 1 + log(tf), ou 0 si tf=0 */
    private static double weightedTf(int tf) {
        return tf > 0 ? 1.0 + Math.log(tf) : 0.0;
    }

    private static Map<String, Integer> getTermFrequencies(String text) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        Analyzer analyzer = new FrenchAnalyzer();  // gère déjà la plupart des stop‑words
        try (var tokenStream = analyzer.tokenStream(null, new StringReader(text))) {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = tokenStream.getAttribute(CharTermAttribute.class).toString();
                if (isInformative(term)) {
                    frequencies.merge(term, 1, Integer::sum);
                }
            }
            tokenStream.end();
        }
        return frequencies;
    }

    /** Filtre les termes trop courts ou dans la liste de stop‑words */
    private static boolean isInformative(String term) {
        return term.length() > 2 && !CUSTOM_STOP_WORDS.contains(term);
    }

    private static double cosineSimilarity(List<Double> vec1, List<Double> vec2) {
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            double v1 = vec1.get(i), v2 = vec2.get(i);
            dot   += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10);
    }
}
