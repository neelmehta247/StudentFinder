/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentfinder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.SwingWorker;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author neel
 */
public class PostRequestSwingWorker extends SwingWorker<Void, Void> {

    private final String className;
    private final JSONObject object;

    public PostRequestSwingWorker(String className, JSONObject object) {
        this.className = className;
        this.object = object;
        System.out.println(object);
    }

    @Override
    protected Void doInBackground() throws MalformedURLException, ProtocolException, JSONException, IOException {
        String url = ParseRequest.baseURL + className.trim();
        URL myURL = new URL(url);

        HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty(ParseRequest.applicationIdHeaderTag, ParseRequest.applicationId);
        connection.setRequestProperty(ParseRequest.restAPIKeyHeaderTag, ParseRequest.restAPIKey);
        connection.setRequestProperty(ParseRequest.contentTypeHeaderTag, ParseRequest.contentType);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(object.toString());
        outputStream.close();

        DataInputStream inputStream = new DataInputStream(connection.getInputStream());

        String result = "";
        for (int i = inputStream.read(); i != -1; i = inputStream.read()) {
            result += (char) i;
        }
        inputStream.close();

        System.out.println("Result: " + result);

        JSONObject json = new JSONObject(result);

        return null;
    }
}
