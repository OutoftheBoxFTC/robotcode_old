package org.ftc7244.datalogger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by BeaverDuck on 10/14/17.
 */

@Target(ElementType.FIELD)
public @interface Logged {
    String name();
}
