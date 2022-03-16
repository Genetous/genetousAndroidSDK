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
    public Login(Activity Act, JSONObject data, String host, PostGet.completionHandler completionHandler){
        this.data=data;
        this.host=host;
        this.Act=Act;
        CompletionHandler = completionHandler;
        this.client= PostGet.URL_TYPE.client;
        this.auth= PostGet.URL_TYPE.auth;
        this.getUser= PostGet.URL_TYPE.getCollection;
        ApplicationInfo ai = null;
        try {
            ai = Act.getPackageManager().getApplicationInfo(Act.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = ai.metaData;
        applicationId = bundle.getString("applicationId");
        organizationId = bundle.getString("organizationId");
    }
    public void process2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject j = getClient();
                if(j!=null){
                    authenticate(j);
                }else{

                }
            }
        }).start();
    }

    public void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject sendObject = new JSONObject();
                try {
                    sendObject.put("application_id", applicationId);
                    sendObject.put("organization_id", organizationId);

                    JSONObject cc = doPost(host + PostGet.urls[client.ordinal()], sendObject, null);
                    JSONObject aa = doPost(host + PostGet.urls[auth.ordinal()], cc, null);
                    if (aa.optString("token") != null) {
                        JSONObject u = doPost(host + PostGet.urls[getUser.ordinal()], data, aa.getString("token"));
                        if (u.optString("id") != null) {
                            sendObject.put("client_id", u.getString("id"));
                            JSONObject j = doPost(host + PostGet.urls[client.ordinal()], sendObject, null);
                            if (j != null) {
                                authenticate(j);
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private JSONObject doPost(String url,JSONObject data,String Token){
        JSONObject jo = null;
        String m = "POST";
        String datar=null;
        final response res;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                doTrustToCertificates();
            }
            URL connectURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            if (Token != null && !Token.isEmpty())
                conn.setRequestProperty("Authorization", Token);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(data.toString());
            os.flush();

            InputStream is = conn.getInputStream();
            datar = convertStreamToString(is);
            jo = new JSONObject(datar);
            return jo;
        } catch (Exception e) {
            return null;
        }
    }
    private JSONObject getClient() {
        JSONObject jo = null;
        String m = "POST";
        String datar=null;
        final response res;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                doTrustToCertificates();
            }
            URL connectURL = new URL(host + PostGet.urls[client.ordinal()]);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(data.toString());
            os.flush();

            InputStream is = conn.getInputStream();
            datar = convertStreamToString(is);
            jo = new JSONObject(datar);
            return jo;
        } catch (Exception e) {
           return null;
        }

    }
    private void authenticate(JSONObject j){
        int responsCode = -1;
        String datar = null;
        JSONObject jo = null;
        String ExceptionData = null;
        response.HttpSuccess result;
        String m = "POST";
        final response res;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                doTrustToCertificates();
            }
            URL connectURL = new URL(host + PostGet.urls[auth.ordinal()]);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(j.toString());
            os.flush();
            responsCode = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            datar = convertStreamToString(is);
            jo = new JSONObject(datar);

            result = response.HttpSuccess.SUCCESS;
        } catch (Exception e) {
            result = response.HttpSuccess.FAIL;
            ExceptionData = e.toString();
        }
        res = new responseBuilder().setResponseCode(responsCode)
                .setJsonData(datar)
                .setExceptionData(ExceptionData)
                .setResult(result)
                .setJsonObject(jo)
                .createResponse();
        Act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CompletionHandler.onHttpFinished(res);
            }
        });
    }
    public static void doTrustToCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
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
