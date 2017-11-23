package org.ftc7244.datalogger;

import org.ftc7244.robotcontroller.Debug;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
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

    private static final long SEND_INTERVAL_MS = 100;

    private HashMap<String, ArrayList<Number>> data;

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
        }
    }

    /**
     * @param tag  identifier for the data
     * @param data data point being added
     * @throws InvalidCharacterException if tag contains ":", which is used for parsing on the
     *                                   receiving end
     */

    public Logger queueData(String tag, Number data) {
        if (running) {
            if (this.data.containsKey(tag)) {
                this.data.get(tag).add(data);
            }
            else this.data.put(tag, new ArrayList<>(Collections.singletonList(data)));
        }
        return this;
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
            if(data.isEmpty()) {
                this.out.println("PING");
            }
            else {
                for (String key : data.keySet()) {
                    for (String out : generateOutput(key)) {
                        this.out.println(out);
                    }
                }
            }
            this.data.clear();
        }
    }

    /**
     * @param key data identifier to reference when generating string
     * @return string containing identifier key and corresponding data points
     */
    private ArrayList<String> generateOutput(String key) {
        ArrayList<String> dataLists = new ArrayList<>();
        ArrayList<Number> data = this.data.get(key);
        int i = 0;
        while (i < data.size()){
            int max = i+100<data.size()?100:data.size()-i;
            String output = key + ":";
            for (int j = 0; j < max; j++) {
                output += truncate(data.get(i+j)) + ":";
            }
            i += max;
            dataLists.add(output);
        }
        return dataLists;
    }

    /**
     * shortens length of sent data string to avoid long messages due to floating point inaccuracy
     *
     * @param raw the raw, unshortened data
     * @return shortened data string
     */

    private String truncate(Number raw) {
        //todo un hardcode
        DecimalFormat format = new DecimalFormat("#.####");
        return format.format(raw);
    }
}