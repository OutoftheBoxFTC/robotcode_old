package org.ftc7244.robotcontroller.sensor;

import java.util.ArrayList;

/*
This filter acts to remove noise in sensor input by giving an average of the previous inputs to a
given length ago.
 */
public class DataFilter{
    private ArrayList<Double> data;
    private int length;
    boolean updating, reading;
    /**
     *
     * @param length Determines how many data points the filter remembers
     */
    public DataFilter(int length){
        this.length = length;
        data = new ArrayList<>();
        updating = false;
        reading = false;
    }

    /**
     *
     * @return average of all remembered data points
     */
    public double getReading(){
        while (updating);
        reading = true;
        double total = 0;
        for(double num : data){
            total += num;
        }
        reading = false;
        return total/length;
    }

    /**
     *
     * @param num raw input directly from the sensor
     */

    public void update(double num) {
        while (reading);
        updating = false;
        if(data.size() >= length){
            data.remove(0);
        }
        data.add(num);
        updating = true;
    }
}