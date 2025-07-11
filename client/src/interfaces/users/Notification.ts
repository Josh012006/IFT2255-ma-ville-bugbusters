
/**
 * Une interface qui représente les notifications de l'utilisateur.
 * Elle garde une trace de l'id de celui à qui la notification est destinée
 * à travers le champ user. Ce champ a la valeur 'STPM' lorsque la notification est à 
 * l'endroit du STPM.
 */
export default interface Notification {
    id?: string,
    user: string,
    message: string,
    createdAt?: Date,
    updatedAt?: Date,
}