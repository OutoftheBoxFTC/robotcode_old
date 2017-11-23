package org.ftc7244.datalogger;

import org.ftc7244.robotcontroller.Debug;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by BeaverDuck on 10/8/17.
 */

public class Logger implements Runnable {
    /**
     * Logger sends sets of data from the used android device to a computer on the receiving port
     * hosting the serverSocket program.
     */
    private static Logger instance = new Logger();

    private static final int PORT = 8709, FIGURES_AFTER_DECIMAL = 4;

    private static final long SEND_INTERVAL_MS = 100, ADD_INTERVAL_MS = 10;

    private HashMap<String, ArrayList<Number>> data;
    private ArrayList<Long> timeStamps;

    private Thread thread;

    private ServerSocket serverSocket;

    private PrintStream out;

    private boolean running;

    public static Logger getInstance() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    private Logger() {
        if (Debug.STATUS) {
            running = true;
            thread = new Thread(this);
            thread.start();
            data = new HashMap<>();
            timeStamps = new ArrayList<>();
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
                int index = new ArrayList<>(this.data.keySet()).indexOf(tag);
                if (System.currentTimeMillis() - timeStamps.get(index) >= ADD_INTERVAL_MS) {
                    this.data.get(tag).add(data);
                    timeStamps.set(index, System.currentTimeMillis());

                }
            } else {
                if (tag.contains(":")) {
                    throw new InvalidCharacterException("Tag cannot contain \":\"");
                }
                this.data.put(tag, new ArrayList<>(Collections.singletonList(data)));
            }
        }
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            Socket client = serverSocket.accept();
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (running) {
            try {
                Thread.sleep(SEND_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HashMap<String, Number> data = (HashMap) this.data.clone();
            for (String key : data.keySet()) {
                out.println(generateOutput(key));
            }
            this.data.clear();
        }
    }

    /**
     * @param key data identifier to reference when generating string
     * @return string containing identifier key and corresponding data points
     */
    private String generateOutput(String key) {
        String out = key + ":";
        for (Number num : data.get(key)) {
            out += truncate(num + "") + ":";
        }
        return out;
    }

    /**
     * shortens length of sent data string to avoid long messages due to floating point inaccuracy
     *
     * @param raw the raw, unshortened data
     * @return shortened data string
     */

    private String truncate(String raw) {
        if (raw.contains(".")) return raw.substring(0, raw.indexOf('.') + FIGURES_AFTER_DECIMAL);
        return raw;
    }
}