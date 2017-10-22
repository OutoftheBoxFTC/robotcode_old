package org.ftc7244.robotcontroller.sensor;

import java.util.ArrayList;

/*
This filter acts to remove noise in sensor input by giving an average of the previous inputs to a
given length ago.
 */
public class DataFilter{
    private ArrayList<Double> data;
    private int length;
    /**
     *
     * @param length Determines how many data points the filter remembers
     */
    public DataFilter(int length){
        this.length = length;
        data = new ArrayList<>();
    }

    /**
     *
     * @return average of all remembered data points
     */
    public double getReading(){
        double total = 0;
        ArrayList<Double> clone = new ArrayList<>();

        for(double num : clone){
            total += num;
        }
        return total/length;
    }

    /**
     *
     * @param num raw input directly from the sensor
     */

    public void update(double num) {
        if(data.size() >= length){
            data.remove(0);
        }
        data.add(num);
    }
}