package org.ftc7244.datalogger;

/**
 * Created by BeaverDuck on 10/8/17.
 */

public class InvalidCharacterException extends IllegalArgumentException {
    public InvalidCharacterException(String details) {
        super(details);
    }
}