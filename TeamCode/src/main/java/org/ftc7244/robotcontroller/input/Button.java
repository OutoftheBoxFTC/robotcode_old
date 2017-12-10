package org.ftc7244.robotcontroller.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A cleaner way of checking if a button is pressed and allow for different button types to be passed
 * around as variables for different button types
 */
public class Button {

    private Gamepad gamepad;
    private ButtonType type;
    private boolean previousState;

    public Button(Gamepad gamepad, ButtonType type) {
        this.gamepad = gamepad;
        this.type = type;
        previousState = false;
    }

    /**
     * Based off the button pressed register the state through different inputs. If it is analog
     * then ensure the value is greater than zero.
     *
     * @return if the button is pressed
     */
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
            case D_PAD_DOWN:
                if (gamepad.dpad_down) return true;
                break;
            case D_PAD_LEFT:
                if (gamepad.dpad_left) return true;
                break;
            case D_PAD_RIGHT:
                if (gamepad.dpad_right) return true;
                break;
            case D_PAD_UP:
                if (gamepad.dpad_up) return true;
                break;
            default:
                return false;
        }
        return false;
    }

    public boolean isUpdated(){
        boolean pressed = isPressed(), returnVal = pressed==previousState;
        previousState = pressed;
        return returnVal;
    }
}
