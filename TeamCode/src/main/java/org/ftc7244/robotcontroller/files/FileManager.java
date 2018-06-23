package org.ftc7244.robotcontroller.files;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ftc72 on 6/18/2018.
 */

public class FileManager {
    Context context;
    AssetManager am;
    BufferedInputStream bufferedInputStream;
    BufferedOutputStream bufferedOutputStream;
    String fileName = "PID_tunings.txt";
    String[] fileList;
    File temp;
    boolean fileExists = false;
    String out;
    public FileManager(Context context){
        this.context = context;
    }

    public void initialize() throws IOException {
        fileList = context.fileList();
        for(int i = 0; i < fileList.length; i ++){
            if(fileList[i] == fileName){
                fileExists = true;
                break;
            }
        }
        if(fileExists){
            bufferedOutputStream = new BufferedOutputStream(context.openFileOutput(fileName, 0));
            bufferedInputStream = new BufferedInputStream(context.openFileInput(fileName));
        }else{
            temp = new File(context.getFilesDir(), fileName);
            bufferedOutputStream = new BufferedOutputStream(context.openFileOutput(fileName, 0));
            bufferedInputStream = new BufferedInputStream(context.openFileInput(fileName));
        }
    }

    public int readFile(byte[] buffer) throws IOException {
        return bufferedInputStream.read(buffer);
    }

    public void writeFile(Double p, Double i, Double d) throws IOException {
        out = (p + "," + i + "," + d);
        bufferedOutputStream.write(out.getBytes(), 0, out.getBytes().length);
    }
}
