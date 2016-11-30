/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentfinder;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author neel
 */
public class GetRequestSwingWorker extends SwingWorker<Void, Void> {

    private final SwingWorkerCallbacks mCallbacks;
    private final ParseRequestTypes requestType;
    private final String[] args;

    public GetRequestSwingWorker(SwingWorkerCallbacks mCallbacks, ParseRequestTypes requestType, String[] args) {
        this.mCallbacks = mCallbacks;
        this.requestType = requestType;
        this.args = args;
    }//TODO put interface and stuff

    @Override
    protected Void doInBackground() throws UnsupportedEncodingException, MalformedURLException, ProtocolException, IOException, JSONException {
        mCallbacks.onDoInBckgroundStarted();

        String url = ParseRequest.baseURL;
        if (requestType == ParseRequestTypes.LOGIN) {
            url += "login?" + URLEncoder.encode("username=" + args[0], "UTF-8") + "&" + URLEncoder.encode("password=" + args[1], "UTF-8");
        } else if (requestType == ParseRequestTypes.VALIDATE_SESSION_TOKEN) {
            url += "users/me";
        } else {
            url += "classes/";
            switch (requestType) {
                case ROOM_MASTER:
                    url += "RoomMaster";
                    break;
                case BLOCKWISE_DATA:
                    url += "BlockwiseDataStudents";
                    break;
                case REROOM:
                    url += "Reroom";
                    break;
                case UPDATED:
                    url += "Updated";
                    break;
            }
            url += "?" + URLEncoder.encode("limit=250", "UTF-8");
        }

        URL myURL = new URL(url);

        HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty(ParseRequest.applicationIdHeaderTag, ParseRequest.applicationId);
        connection.setRequestProperty(ParseRequest.restAPIKeyHeaderTag, ParseRequest.restAPIKey);
        connection.setRequestProperty(ParseRequest.contentTypeHeaderTag, ParseRequest.contentType);

        if (requestType == ParseRequestTypes.VALIDATE_SESSION_TOKEN) {
            connection.setRequestProperty(ParseRequest.sessionHeaderTag, args[0]);
        }

        connection.setDoInput(true);
        connection.setDoOutput(false);

        DataInputStream inputStream;

        if (connection.getResponseCode() >= 400) {
            inputStream = new DataInputStream(connection.getErrorStream());
        } else {
            inputStream = new DataInputStream(connection.getInputStream());
        }

        String result = "";
        for (int i = inputStream.read(); i != -1; i = inputStream.read()) {
            result += (char) i;
        }
        inputStream.close();

        mCallbacks.onDone(new JSONObject(result), requestType);

        return null;
    }

}
