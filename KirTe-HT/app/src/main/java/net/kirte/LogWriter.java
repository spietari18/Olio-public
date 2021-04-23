package net.kirte;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class LogWriter {
    private final String filename = "log.txt";
    private static final LogWriter log = new LogWriter();

    public static LogWriter getInstance() {
        return log;
    }

    public ArrayList<String> getPayments() {
        ArrayList<String> payments = new ArrayList<String>();
        Context context = MainActivity.getInstance();
        try {
            InputStream ins = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String row;
            while ((row = br.readLine()) != null) {
                payments.add(row);
            }
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public void log(String refNum, String price) {
        Context context = MainActivity.getInstance();
        DateTimeFormatter fm = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String sNow = fm.format(now);
        //String today = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(LocalDate.now(ZoneId.of("Europe/Helsinki")));
        String toWrite = "[" + sNow + "] " + refNum + ": " + price + "\n";
        try {
            OutputStreamWriter ous = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_APPEND));
            ous.append(toWrite);
            ous.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("New line added to the log.");
        }
    }
}
