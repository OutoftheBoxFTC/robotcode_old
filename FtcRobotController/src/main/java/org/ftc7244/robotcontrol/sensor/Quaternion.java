package org.ftc7244.robotcontrol.sensor;

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
    public void set(Quaternion quat) {
        this.points[0] = quat.points[0];
        this.points[1] = quat.points[1];
        this.points[2] = quat.points[2];
        this.points[3] = quat.points[3];
    }

    /**
     * Multiply this quaternion by the input quaternion and store the result in the out quaternion
     *
     * @param input
     * @param output
     */
    public void multiplyByQuat(Quaternion input, Quaternion output) {

        if (input != output) {
            output.points[3] = (points[3] * input.points[3] - points[0] * input.points[0] - points[1] * input.points[1] - points[2]
                    * input.points[2]); //getW = w1w2 - x1x2 - y1y2 - z1z2
            output.points[0] = (points[3] * input.points[0] + points[0] * input.points[3] + points[1] * input.points[2] - points[2]
                    * input.points[1]); //getX = w1x2 + x1w2 + y1z2 - z1y2
            output.points[1] = (points[3] * input.points[1] + points[1] * input.points[3] + points[2] * input.points[0] - points[0]
                    * input.points[2]); //setY = w1y2 + y1w2 + z1x2 - x1z2
            output.points[2] = (points[3] * input.points[2] + points[2] * input.points[3] + points[0] * input.points[1] - points[1]
                    * input.points[0]); //setZ = w1z2 + z1w2 + x1y2 - y1x2
        } else {
            Quaternion tmpVector = new Quaternion();
            tmpVector.points[0] = input.points[0];
            tmpVector.points[1] = input.points[1];
            tmpVector.points[2] = input.points[2];
            tmpVector.points[3] = input.points[3];

            output.points[3] = (points[3] * tmpVector.points[3] - points[0] * tmpVector.points[0] - points[1]
                    * tmpVector.points[1] - points[2] * tmpVector.points[2]); //getW = w1w2 - x1x2 - y1y2 - z1z2
            output.points[0] = (points[3] * tmpVector.points[0] + points[0] * tmpVector.points[3] + points[1]
                    * tmpVector.points[2] - points[2] * tmpVector.points[1]); //getX = w1x2 + x1w2 + y1z2 - z1y2
            output.points[1] = (points[3] * tmpVector.points[1] + points[1] * tmpVector.points[3] + points[2]
                    * tmpVector.points[0] - points[0] * tmpVector.points[2]); //setY = w1y2 + y1w2 + z1x2 - x1z2
            output.points[2] = (points[3] * tmpVector.points[2] + points[2] * tmpVector.points[3] + points[0]
                    * tmpVector.points[1] - points[1] * tmpVector.points[0]); //setZ = w1z2 + z1w2 + x1y2 - y1x2
        }
    }

    public float getX() {
        return this.points[0];
    }

    public float getY() {
        return this.points[1];
    }

    public float getZ() {
        return this.points[2];
    }

    public float getW() {
        return this.points[3];
    }

    public float setX(float x) {
        return this.points[0] = x;
    }

    public float setY(float y) {
        return this.points[1] = y;
    }

    public float setZ(float z) {
        return this.points[2] = z;
    }

    public float setW(float w) {
        return this.points[3] = w;
    }


    @Override
    public String toString() {
        return "X:" + points[0] + " Y:" + points[1] + " Z:" + points[2] + " W:" + points[3];
    }
}