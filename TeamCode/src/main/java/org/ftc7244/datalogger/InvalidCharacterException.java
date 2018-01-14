package org.ftc7244.datalogger;

/**
 * Created by BeaverDuck on 10/8/17.
 */

/**
 * Thrown when the tag of a given data point contains a character used for data organization and
 * interpretation by the desktop program
 */
public class InvalidCharacterException extends IllegalArgumentException {
    public InvalidCharacterException(String details) {
        super(details);
    }
}