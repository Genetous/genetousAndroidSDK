package com.mubo.genetous_http;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Login {
    private JSONObject data;
    private String host;
    private PostGet.URL_TYPE client;
    private PostGet.URL_TYPE auth;
    private PostGet.URL_TYPE getUser;
    private Activity Act;
    private PostGet.completionHandler CompletionHandler;
    private String applicationId;
    private String organizationId;
    private String clientSecret;
    public Login(Activity Act, JSONObject data, String host, PostGet.completionHandler completionHandler){
        this.data=data;
        this.host=host;
        this.Act=Act;
        CompletionHandler = completionHandler;
        this.client= PostGet.URL_TYPE.client;
        this.auth= PostGet.URL_TYPE.auth;
        this.getUser= PostGet.URL_TYPE.getCollections;
        ApplicationInfo ai = null;
        try {
            ai = Act.getPackageManager().getApplicationInfo(Act.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = ai.metaData;
        applicationId = bundle.getString("applicationId");
        organizationId = bundle.getString("organizationId");
        clientSecret = bundle.getString("secretKey");
    }

    public void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject sendObject = new JSONObject();
                try {
                    sendObject.put("applicationId", applicationId);
                    sendObject.put("organizationId", organizationId);
                    JSONObject cc = doPost(host + PostGet.urls[client.ordinal()], sendObject, null);
                    if (cc.has("success") && cc.getBoolean("success") == false) {
                        sendError(cc);
                    }
                    cc.put("clientSecret",clientSecret);
                    JSONObject aa = doPost(host + PostGet.urls[auth.ordinal()], cc, null);
                    if (aa.has("success") && aa.getBoolean("success") == false) {
                        sendError(aa);
                    }
                    JSONObject u = doPost(host + PostGet.urls[getUser.ordinal()], data, aa.getString("token"));
                    if (u.has("success") && u.getBoolean("success") == false) {
                        sendError(u);
                    }
                    if(u.has("data") && u.getJSONArray("data").length()<1){
                        JSONObject r=new JSONObject();
                        r.put("success",false);
                        r.put("code",404);
                        r.put("message","No user found!");
                        sendError(r);
                    }

                    if (u.getJSONArray("data").getJSONObject(0).has("_id")) {
                        sendObject.put("clientId", u.getJSONArray("data").getJSONObject(0).getString("_id"));
                        JSONObject j = doPost(host + PostGet.urls[client.ordinal()], sendObject, null);
                        if (j.has("success") && j.getBoolean("success") == false) {
                            sendError(j);
                        }
                        j.put("clientSecret",clientSecret);
                        j = doPost(host + PostGet.urls[auth.ordinal()], j, null);
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
                        //authenticate(j);
                    } else {
                        response res = new responseBuilder()
                                .setExceptionData("Client Not Found!")
                                .createResponse();
                        Act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CompletionHandler.onHttpFinished(res);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
    public static JSONObject doPost(String url,JSONObject data,String Token) throws JSONException {
        JSONObject jo = null;
        String m =  data!=null?"POST":"GET";
        String datar=null;
        final response res;
        try {
            URL connectURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            conn.setDoInput(true);
            if(data!=null)
                conn.setDoOutput(true);
            conn.setUseCaches(false);
            if (Token != null && !Token.isEmpty())
                conn.setRequestProperty("Authorization", Token);
            if(data!=null) {
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(data.toString());
                os.flush();
            }
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

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
