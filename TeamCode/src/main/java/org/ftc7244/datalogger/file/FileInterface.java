package org.ftc7244.datalogger.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by BeaverDuck on 12/6/17.
 */

public class FileInterface implements Runnable {
    private static FileInterface instance;
    private BufferedReader in;
    private boolean running;
    private Thread thread;
    private Context context;
    private ArrayList<String> valueOrder = new ArrayList<>();

    private FileInterface(Context context){
        this.context = context;
    }

    void start(BufferedReader in){
        this.in = in;
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public static FileInterface getInstance() {
        return instance;
    }

    public static void init(Context context){
        if(instance==null) instance = new FileInterface(context);
    }

    public ArrayList<String> readLines(String path){
        try {
            Scanner input = new Scanner(context.openFileInput(path));
            ArrayList<String> lines = new ArrayList<>();
            while (input.hasNextLine()){
                lines.add(input.nextLine());
            }
            return lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeToFile(String path, boolean overwrite, ArrayList<String> lines){
        try {
            if(overwrite)
                new File(path).createNewFile();

            FileOutputStream stream = context.openFileOutput(path, Context.MODE_PRIVATE);
            for(String line : lines){
                stream.write((line + System.getProperty("line.separator")).getBytes());
            }
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
