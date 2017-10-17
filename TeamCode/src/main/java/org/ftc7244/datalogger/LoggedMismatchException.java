package org.ftc7244.datalogger;

/**
 * Created by BeaverDuck on 10/14/17.
 */

public class LoggedMismatchException extends IllegalArgumentException {
    public LoggedMismatchException(String message){
        super(message);
    }
}