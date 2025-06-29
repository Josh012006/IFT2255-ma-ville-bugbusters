package ca.udem.maville.utils;

import java.util.UUID;

public final class UniqueID {

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

}
