package org.ftc7244.robotcontroller.sensor;

import java.util.ArrayList;

/*
This filter acts to remove noise in sensor input by giving an average of the previous inputs to a
given length ago.
 */
public class DataFilter {
    private ArrayList<Double> data;
    private int length;
    private volatile double reading;

    /**
     * @param length Determines how many data points the filter remembers
     */
    public DataFilter(int length) {
        this.length = length;
        data = new ArrayList<>();
    }

    /**
     * @return average of all remembered data points
     */
    public double getReading() {
        return reading;
    }

    /**
     * @param num raw input directly from the sensor
     */

    public void update(double num) {
        if (data.size() >= length) {
            data.remove(0);
        }
        data.add(num);
        double total = 0;
        for (double point : data) {
            total += point;
        }
        reading = total / length;
    }
}