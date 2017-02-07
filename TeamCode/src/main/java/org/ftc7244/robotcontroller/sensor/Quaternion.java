package org.ftc7244.robotcontroller.sensor;

import android.support.annotation.NonNull;

/**
 * Representation of a four-dimensional float-vector
 */
public class Quaternion {

    private float points[];

    /**
     * Instantiates a new vector4f.
     */
    public Quaternion() {
        points = new float[]{0, 0, 0, 1};
    }

    /**
     * To array.
     *
     * @return the float[]
     */
    public float[] array() {
        return points;
    }

    /**
     * Copies the values from the given quaternion to this one
     *
     * @param quat The quaternion to copy from
     */
    public void set(@NonNull Quaternion quat) {
        this.points[0] = quat.points[0];
        this.points[1] = quat.points[1];
        this.points[2] = quat.points[2];
        this.points[3] = quat.points[3];
    }

    /**
     * Multiply this quaternion by the input quaternion and store the result in the out quaternion
     *
     * @param input the quaternion to multiply with
     * @param output the quaternion where the values are saved
     */
    public void multiplyByQuat(@NonNull Quaternion input, @NonNull Quaternion output) {

        if (input != output) {
            //getW = w1w2 - x1x2 - y1y2 - z1z2
            output.points[3] = (points[3] * input.points[3] - points[0] * input.points[0] - points[1] * input.points[1] - points[2] * input.points[2]);
            //getX = w1x2 + x1w2 + y1z2 - z1y2
            output.points[0] = (points[3] * input.points[0] + points[0] * input.points[3] + points[1] * input.points[2] - points[2] * input.points[1]);
            //setY = w1y2 + y1w2 + z1x2 - x1z2
            output.points[1] = (points[3] * input.points[1] + points[1] * input.points[3] + points[2] * input.points[0] - points[0] * input.points[2]);
            //setZ = w1z2 + z1w2 + x1y2 - y1x2
            output.points[2] = (points[3] * input.points[2] + points[2] * input.points[3] + points[0] * input.points[1] - points[1] * input.points[0]);
        } else {
            Quaternion tmpVector = new Quaternion();
            tmpVector.points[0] = input.points[0];
            tmpVector.points[1] = input.points[1];
            tmpVector.points[2] = input.points[2];
            tmpVector.points[3] = input.points[3];

            //getW = w1w2 - x1x2 - y1y2 - z1z2
            output.points[3] = (points[3] * tmpVector.points[3] - points[0] * tmpVector.points[0] - points[1] * tmpVector.points[1] - points[2] * tmpVector.points[2]);
            //getX = w1x2 + x1w2 + y1z2 - z1y2
            output.points[0] = (points[3] * tmpVector.points[0] + points[0] * tmpVector.points[3] + points[1] * tmpVector.points[2] - points[2] * tmpVector.points[1]);
            //setY = w1y2 + y1w2 + z1x2 - x1z2
            output.points[1] = (points[3] * tmpVector.points[1] + points[1] * tmpVector.points[3] + points[2] * tmpVector.points[0] - points[0] * tmpVector.points[2]);
            //setZ = w1z2 + z1w2 + x1y2 - y1x2
            output.points[2] = (points[3] * tmpVector.points[2] + points[2] * tmpVector.points[3] + points[0] * tmpVector.points[1] - points[1] * tmpVector.points[0]);
        }
    }

    /**
     * Gets the X value of the quaternion
     * @return value in radians
     */
    public float getX() {
        return this.points[0];
    }

    /**
     * Gets the Y value of the quaternion
     * @return value in radians
     */
    public float getY() {
        return this.points[1];
    }

    /**
     * Gets the Z value of the quaternion
     * @return value in radians
     */
    public float getZ() {
        return this.points[2];
    }

    /**
     * Gets the W value of the quaternion
     * @return from -1 to 1
     */
    public float getW() {
        return this.points[3];
    }

    /**
     * Updates the X value of the quaternion
     * @param x value in radians
     * @return new value in radians
     */
    public float setX(float x) {
        return this.points[0] = x;
    }

    /**
     * Updates the Y value of the quaternion
     * @param y value in radians
     * @return new value in radians
     */
    public float setY(float y) {
        return this.points[1] = y;
    }

    /**
     * Updates the Z value of the quaternion
     * @param z value in radians
     * @return new value in radians
     */
    public float setZ(float z) {
        return this.points[2] = z;
    }

    /**
     * Updates the W value of the quaternion
     * @param w from -1 to 1
     * @return the new value
     */
    public float setW(float w) {
        return this.points[3] = w;
    }


    @NonNull
    @Override
    public String toString() {
        return "X:" + points[0] + " Y:" + points[1] + " Z:" + points[2] + " W:" + points[3];
    }
}