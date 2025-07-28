import natural from "natural";

/**
 * Cette fonction permet de dire si deux textes portent sur le même sujet. Ell sera utilisée pour savoir si une fciche problème
 * similaire existe déjà pour un signalement.
 * @param descriptionA le premier texte
 * @param descriptionB le second texte
 */
export function isSimilar (descriptionA: string, descriptionB: string) : boolean {
    const tfidf = new natural.TfIdf();

    tfidf.addDocument(descriptionA);
    tfidf.addDocument(descriptionB);

    const vectorA : Record<string, number> = tfidf.listTerms(0).reduce((acc, t) => ({ ...acc, [t.term]: t.tfidf }), {});
    const vectorB : Record<string, number> = tfidf.listTerms(1).reduce((acc, t) => ({ ...acc, [t.term]: t.tfidf }), {});

    // Calcul de la similarité cosinus
    const allTerms = new Set([...Object.keys(vectorA), ...Object.keys(vectorB)]);
    const dot = [...allTerms].reduce((sum, key) => sum + (vectorA[key] || 0) * (vectorB[key] || 0), 0);
    const normA = Math.sqrt([...allTerms].reduce((sum, key) => sum + (vectorA[key] || 0) ** 2, 0));
    const normB = Math.sqrt([...allTerms].reduce((sum, key) => sum + (vectorB[key] || 0) ** 2, 0));
    const similarity = dot / (normA * normB);

    return (similarity >= 0.6)
}
