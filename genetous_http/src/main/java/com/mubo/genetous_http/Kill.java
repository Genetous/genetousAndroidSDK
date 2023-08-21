package com.mubo.genetous_http;

import static com.mubo.genetous_http.Login.convertStreamToString;

import android.app.Activity;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Kill {
    private String token;
    private String host;
    private PostGet.URL_TYPE kill;
    private Activity Act;
    private PostGet.completionHandler CompletionHandler;
    public Kill(Activity Act, String token, String host, PostGet.completionHandler completionHandler){
        this.token=token;
        this.host=host;
        this.Act=Act;
        CompletionHandler = completionHandler;
        this.kill= PostGet.URL_TYPE.killToken;
    }
    public void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    JSONObject j= doPost(host + PostGet.urls[kill.ordinal()],"Bearer "+token);
                    if (j.has("success") && j.getBoolean("success") == false) {
                        sendError(j);
                    }
                    response res = new responseBuilder().setResponseCode(200)
                            .setJsonData(j.toString())
                            .setExceptionData(null)
                            .setResult(response.HttpSuccess.SUCCESS)
                            .setJsonObject(j)
                            .createResponse();
                    Act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CompletionHandler.onHttpFinished(res);
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
    public static JSONObject doPost(String url,String Token) throws JSONException {
        JSONObject jo = null;
        String m = "POST";
        String datar=null;
        final response res;
        try {
            URL connectURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            if (Token != null && !Token.isEmpty())
                conn.setRequestProperty("Authorization", Token);
            int code = conn.getResponseCode();
            InputStream is=null;
            if(code!=200) {
                if(code==500){
                    JSONObject err500=new JSONObject();
                    err500.put("success",false);
                    err500.put("message","Unexpected Error Occured!");
                    err500.put("code",code);
                    return err500;
                }
                is = conn.getErrorStream();
            }else {
                is = conn.getInputStream();
            }
            datar = convertStreamToString(is);
            jo = new JSONObject(datar);
            if(code!=200)
                jo.put("code",code);
            return jo;
        } catch (Exception e) {
            JSONObject err500=new JSONObject();
            err500.put("success",false);
            err500.put("message","Unexpected Error Occured!");
            err500.put("code",500);
            return err500;
        }
    }
    void sendError(JSONObject err) throws JSONException {

        response res = new responseBuilder()
                .setExceptionData(err.getString("message"))
                .setResult(response.HttpSuccess.FAIL)
                .setResponseCode(err.getInt("code"))
                .createResponse();
        Act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CompletionHandler.onHttpFinished(res);
            }
        });

    }
}
