package org.ftc7244.robotcontroller.files;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ftc72 on 6/18/2018.
 */

public class fileManager {
    AssetManager am;
    BufferedInputStream bufferedInputStream;
    BufferedOutputStream bufferedOutputStream;
    public fileManager(){

    }

    public void initialize(Context context) throws IOException {
        am = context.getAssets();
        bufferedInputStream = new BufferedInputStream(am.open("test.txt"));
    }

    public int readFile(byte[] buffer) throws IOException {
        return bufferedInputStream.read(buffer);
    }

    public void writeFile(String str){

    }
}
