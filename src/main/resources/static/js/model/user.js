export class User {
    /**
     * @type string
     */
    id;
    /**
     * @type string
     */
    username;
    /**
     * Only set for the current user. Other user objects will not contain an email for privacy reasons
     * @type string
     */
    email;
}