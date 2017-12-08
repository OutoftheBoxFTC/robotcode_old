package org.ftc7244.datalogger.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BeaverDuck on 12/6/17.
 */

public class FileInterface implements Runnable {
    private BufferedReader in;
    private boolean running;
    private Thread thread;
    private ArrayList<String> valueOrder = new ArrayList<>();

    public FileInterface(BufferedReader in){
        this.in = in;
        running = true;

        thread.start();
    }

    public static ArrayList<String> readLines(String path){
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            ArrayList<String> lines = new ArrayList<>();
            while (line != null){
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeToFile(String path, boolean overwrite, ArrayList<String> lines){
        try {
            File file = new File(path);
            if(file.exists()&&overwrite)file.delete();
            else file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for(String line : lines){
                writer.write(line);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running && !thread.isInterrupted()){
            try {
                String in = this.in.readLine(),
                tag = in.substring(0, in.indexOf(':'));
                double value = Double.parseDouble(in.substring(in.indexOf(':')+1));
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
