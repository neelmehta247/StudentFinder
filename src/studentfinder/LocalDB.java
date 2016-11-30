/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentfinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author neel
 */
public class LocalDB {

    public static final String FILE_SESSION_TOKEN = "session.txt";
    public static final String FILE_BLOCKWISE_DATA = "blockwise.csv";
    public static final String FILE_ROOM_MASTER = "roommaster.csv";
    public static final String FILE_REROOM = "reroom.csv";
    public static final String FILE_LAST_UPDATE = "lastupdate.txt";

    public static File getFile(String filename) {
        try {
            return new File(filename);
        } catch (Exception e) {
            return null;
        }
    }

    public static File makeCSV(String filename, JSONObject json, String[] columns) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) {
            for (int i = 0; i < columns.length; i++) {
                if (i != columns.length - 1) {
                    writer.write(columns[i] + " ,");
                } else {
                    writer.write(columns[i] + "\n");
                }
            }
            JSONArray array = json.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                for (int j = 0; j < columns.length; j++) {
                    if (j != columns.length - 1) {
                        try {
                            writer.write(optimiseText(obj.getString(columns[j])) + ",");
                        } catch (JSONException ex) {
                            try {
                                writer.write(optimiseText(String.valueOf(obj.getInt(columns[j])) + ","));
                            } catch (JSONException e) {
                                writer.write(" ,");
                            }
                        }
                    } else {
                        try {
                            writer.write(obj.getString(columns[j]) + "\n"); //Not calling optimise since objectId has to be normal
                        } catch (JSONException ex) {
                            writer.write(" \n");
                        }
                    }
                }
            }
            writer.close();

            writeFile(FILE_LAST_UPDATE, String.valueOf(System.currentTimeMillis() / 1000));
        } catch (Exception e) {
        }

        return new File(filename);
    }

    public static void writeFile(String filename, String text) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) {
            writer.write(text);
        } catch (Exception e) {
        }
    }

    public static String optimiseText(String input) {
        String output = "";

        if (input == null) {
            return "";
        }

        input = input.trim();

        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (x == '\n') {
                output += "|";
            } else {
                output += x;
            }
        }

        return output.toUpperCase() + " ";
    }
}
