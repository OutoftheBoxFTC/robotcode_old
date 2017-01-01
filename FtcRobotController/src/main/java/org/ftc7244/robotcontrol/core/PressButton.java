package org.ftc7244.robotcontrol.core;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by OOTB on 10/30/2016.
 */

public class PressButton extends Button {

    private boolean active, pressed;

    public PressButton(Gamepad gamepad, ButtonType type) {
        super(gamepad, type);
        active = false;
        pressed = false;
    }

    @Override
    public boolean isPressed() {
        boolean state = super.isPressed();
        if (!pressed && state) {
            pressed = true;
        } else if (pressed && !state) {
            active = !active;
            pressed = false;
        }
        return active;
    }
}
