package org.ftc7244.robotcontrol.core;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by OOTB on 10/30/2016.
 */

public class PressButton extends Button {

    private boolean active;

    public PressButton(Gamepad gamepad, ButtonType type) {
        super(gamepad, type);
    }

    @Override
    public boolean isPressed() {
        boolean pressed = super.isPressed();
        if (pressed && !active) {
            active = true;
        }
        if (!pressed && active) {
            active = false;
            return true;
        }
        return false;
    }
}
