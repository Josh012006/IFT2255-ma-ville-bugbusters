
/**
 * An interface that represents the notifications.
 */
export default interface Notification {
    id?: string,
    user: string,
    message: string,
    createdAt?: Date,
    updatedAt?: Date,
}