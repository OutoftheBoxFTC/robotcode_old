package org.ftc7244.datalogger.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by BeaverDuck on 12/6/17.
 */

public class FileInterface {
    public ArrayList<String> readLines(String path){
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
