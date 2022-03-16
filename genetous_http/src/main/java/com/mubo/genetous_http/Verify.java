package com.mubo.genetous_http;

import android.app.Activity;
import android.os.Build;

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

public class Verify {
    private String token;
    private String host;
    private PostGet.URL_TYPE verify;
    private Activity Act;
    private PostGet.completionHandler CompletionHandler;
    public Verify(Activity Act, String token, String host, PostGet.completionHandler completionHandler){
        this.token=token;
        this.host=host;
        this.Act=Act;
        CompletionHandler = completionHandler;
        this.verify= PostGet.URL_TYPE.verify;
    }
    public void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                verify();
            }
        }).start();
    }
    private void verify() {
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
            URL connectURL = new URL(host + PostGet.urls[verify.ordinal()]);
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", token);

            InputStream is = conn.getInputStream();
            datar = convertStreamToString(is);
            jo = new JSONObject(datar);
            if(jo.optString("success")!=null){
                result = response.HttpSuccess.FAIL;
                ExceptionData = "Token not found";
            }else {
                result = response.HttpSuccess.SUCCESS;
            }
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
