package org.ftc7244.robotcontroller.files;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ftc72 on 6/18/2018.
 */

public class FileManager {
    Context context;
    AssetManager am;
    public BufferedInputStream bufferedInputStream;
    public BufferedOutputStream bufferedOutputStream;
    String fileName = "PID_tunings.txt";
    public String[] fileList;
    byte[] temp;
    File file;
    public boolean fileExists = false;
    String out;
    public FileManager(Context context){
        this.context = context;
    }

    public void initialize() throws IOException, NullPointerException {
        fileList = context.fileList();
        for(int i = 0; i < fileList.length; i ++){
            if(fileList[i].compareTo(fileName) == 0){
                fileExists = true;
                break;
            }
        }
        file = new File(context.getFilesDir(), fileName);
        bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(context.getFilesDir() + "/" + fileName)));
        bufferedInputStream.mark(5);
    }

    public void startWriting() throws IOException {
        try {
            bufferedInputStream.read(temp);
        }catch(NullPointerException e){
            temp = null;
        }
        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(context.getFilesDir() + "/" + fileName)));
        if(temp != null) {
            bufferedOutputStream.write(temp);
        }
    }

    public int readFile(byte[] buffer) throws IOException {
        bufferedInputStream.reset();
        return bufferedInputStream.read(buffer);
    }

    public void writeFile(Double p, Double i, Double d) throws IOException {
        out = (p + "," + i + "," + d);
        bufferedOutputStream.write(out.getBytes(), 0, out.getBytes().length);
        bufferedOutputStream.flush();
    }

    public void closeFile() throws IOException {
        bufferedOutputStream.close();
    }
}