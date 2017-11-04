package org.ftc7244.datalogger;

import android.os.AsyncTask;

import org.ftc7244.robotcontroller.Debug;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by BeaverDuck on 10/8/17.
 */

public class Logger implements Runnable {
    /**
     * Logger sends sets of data from the used android device to a computer on the receiving port
     * hosting the serverSocket program.
     */
    private static Logger instance;

    private static final int PORT = 8709;

    private static final long SEND_INTERVAL_MS = 100;

    private ConcurrentHashMap<String, List<Number>> data;

    private ServerSocket serverSocket;

    private PrintStream out;

    private boolean running;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private Logger() {
        System.out.println("Working");
        if (Debug.STATUS) {
            data = new ConcurrentHashMap<>();
            running = true;
            AsyncTask.execute(this);
        }
    }

    /**
     * @param tag  identifier for the data
     * @param data data point being added
     * @throws InvalidCharacterException if tag contains ":", which is used for parsing on the
     *                                   receiving end
     */

    public void addData(String tag, Number data) {
        if (running) {
            if (this.data.containsKey(tag)) {
                this.data.get(tag).add(data);
            } else {
                if (tag.contains(":"))
                    throw new InvalidCharacterException("Tag cannot contain \":\"");
                this.data.put(tag, Collections.synchronizedList(new ArrayList<Number>()));
                addData(tag, data);
            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                serverSocket = new ServerSocket(PORT);
                Socket client = serverSocket.accept();
                System.out.println(serverSocket.isClosed());
                out = new PrintStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (running) {
                System.out.println("Working");
                for (String key : data.keySet()) {
                    out.print(generateOutput(key));
                }
                out.flush();
                data.clear();
            }
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param key data identifier to reference when generating string
     * @return string containing identifier key and corresponding data points
     */
    private String generateOutput(String key) {
        String out = key + ":";
        for (Number num : data.get(key)) {
            DecimalFormat formatter = new DecimalFormat("#.##");
            out += formatter.format(num) + ":";
        }
        return out;
    }
}