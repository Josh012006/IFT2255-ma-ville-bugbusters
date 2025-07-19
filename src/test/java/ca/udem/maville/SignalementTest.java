package ca.udem.maville;

import org.junit.jupiter.api.Test;
import ca.udem.maville.client.Signalement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test unitaire pour la classe Signalement.
 * Objectif : vérifier la validité de l’instanciation et des attributs clés.
 */
public class SignalementTest {

    /**
     * Test 1 — Création valide :
     * Vérifie que le constructeur initialise correctement tous les champs avec des valeurs attendues.
     * Résultat attendu : tous les getters retournent les bonnes valeurs passées en paramètre.
     */
    @Test
    public void testCreationSignalementValide() {
        Signalement s = new Signalement(
            "Trou dans la chaussée",
            "Rue Ontario",
            "Fissure trottoir",
            "res123",
            "Ville-Marie",
            "sig123"
        );
        assertEquals("Ville-Marie", s.getQuartier());
        assertEquals("Fissure trottoir", s.getDescription());
        assertEquals("Trou dans la chaussée", s.getTypeProbleme());
    }

    /**
     * Test 2 — Description vide :
     * Vérifie qu’une IllegalArgumentException est levée si la description est vide.
     * Résultat attendu : le message de l’exception doit contenir le mot "description".
     * ⚠️ Pour que ce test fonctionne, le constructeur de Signalement doit vérifier la description comme :
     *     if (description == null || description.isEmpty()) throw new IllegalArgumentException("description invalide");
     */
    @Test
    public void testSignalementAvecDescriptionVide() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Signalement(
                "Problème X",
                "Rue Berri",
                "",
                "res123",
                "Ville-Marie",
                "sig124"
            );
        });
        assertTrue(exception.getMessage().toLowerCase().contains("description"));
    }

    /**
     * Test 3 — Date automatique :
     * Vérifie que la date du signalement est bien générée automatiquement à la création.
     * Résultat attendu : la date ne doit pas être null.
     */
    @Test
    public void testDateSignalementNonNulle() {
        Signalement s = new Signalement(
            "Signalisation manquante",
            "Rue Saint-Denis",
            "Panneau tombé",
            "res124",
            "Rosemont",
            "sig125"
        );
        assertNotNull(s.getDateSignalement());
    }
}
