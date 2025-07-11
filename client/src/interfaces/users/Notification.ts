
/**
 * An interface that represents the notifications.
 */
export default interface Notification {
    _id?: string,
    user: string,
    message: string,
    createdAt?: Date,
    updatedAt?: Date,
}