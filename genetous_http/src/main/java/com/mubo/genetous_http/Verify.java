package com.mubo.genetous_http;

import static com.mubo.genetous_http.Login.doPost;

import android.app.Activity;
import android.os.Build;

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

                try {
                    JSONObject j= doPost(host + PostGet.urls[verify.ordinal()],null,"Bearer "+token);
                    if (j.has("success") && j.getBoolean("success") == false) {
                        sendError(j);
                        return;
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

    void sendError(JSONObject err) throws JSONException {

        response res = new responseBuilder()
                .setExceptionData(err.has("message")?err.getString("message"):err.toString())
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
