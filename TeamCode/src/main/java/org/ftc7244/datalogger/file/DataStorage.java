package org.ftc7244.datalogger.file;

import java.util.ArrayList;

public class DataStorage {
    private ArrayList<Double> data;
    private ArrayList<DataStorage> subStorage;

    public DataStorage(String parse){
        data = new ArrayList<>();
        subStorage = new ArrayList<>();
        parse(parse);
    }

    public DataStorage(){
        data = new ArrayList<>();
        subStorage = new ArrayList<>();
    }

    private void parse(String input){
        int subStartIndex = input.indexOf('(')-1;
        String[] splitData = input.substring(0, subStartIndex).split("/");
        for (String data : splitData) {
            this.data.add(Double.parseDouble(data));
        }
        parseSub(input.substring(subStartIndex));
    }

    private void parseSub(String sub){
        String current = "";
        int level = 0;
        for (int i = 0; i < sub.length(); i++) {
            if(sub.charAt(i)=='(')level++;
            else if(sub.charAt(i)==')'){
                level--;
                if(level==0){
                    subStorage.add(new DataStorage(current));
                    current = "";
                }
            }
            else if(level>0)current+=sub.charAt(i);
        }
    }

    public String toString(){
        String out = "";
        for (double num: data){
            out += num + "/";
        }

        for (DataStorage storage : subStorage){
            out += "(" + storage.toString() + ")/";
        }
        return out;
    }

    public double get(int index){
        return data.get(index);
    }

    public void add(double num){
        data.add(num);
    }

    public void remove(int index){
        data.remove(index);
    }

    public void set(int index, double num){
        data.set(index, num);
    }

    public DataStorage getSubStorage(int index){
        return subStorage.get(index);
    }

    public void addSubStorage(DataStorage storage){
        subStorage.add(storage);
    }

    public void removeSubStorage(int index){
        subStorage.remove(index);
    }

    public void clearData(){
        data.clear();
    }

    public void clearSubStorage(){
        subStorage.clear();
    }

    public int size(){
        return data.size();
    }

    public int subStorages(){
        return subStorage.size();
    }
}
