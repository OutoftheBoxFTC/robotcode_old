package org.ftc7244.robotcontroller.core;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Button {

    private Gamepad gamepad;
    private ButtonType type;

    public Button(Gamepad gamepad, ButtonType type) {
        this.gamepad = gamepad;
        this.type = type;
    }

    public boolean isPressed() {
        switch (type) {
            case A:
                if (gamepad.a) return true;
                break;
            case B:
                if (gamepad.b) return true;
                break;
            case X:
                if (gamepad.x) return true;
                break;
            case Y:
                if (gamepad.y) return true;
                break;
            case LEFT_TRIGGER:
                if (gamepad.left_trigger > 0) return true;
                break;
            case RIGHT_TRIGGER:
                if (gamepad.right_trigger > 0) return true;
                break;
            case LEFT_BUMPER:
                if (gamepad.left_bumper) return true;
                break;
            case RIGHT_BUMPER:
                if (gamepad.right_bumper) return true;
                break;
            default:
                return false;
        }
        return false;
    }
}
