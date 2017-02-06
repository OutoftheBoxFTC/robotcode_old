package org.ftc7244.robotcontroller.core;

import com.qualcomm.robotcore.hardware.Gamepad;

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
