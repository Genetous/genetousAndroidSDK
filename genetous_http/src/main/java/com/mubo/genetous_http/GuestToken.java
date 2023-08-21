package com.mubo.genetous_http;

import static com.mubo.genetous_http.Login.doPost;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

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

public class GuestToken {
    private String host;
    private PostGet.URL_TYPE client;
    private PostGet.URL_TYPE auth;
    private PostGet.URL_TYPE getUser;
    private Activity Act;
    private PostGet.completionHandler CompletionHandler;
    private String applicationId;
    private String organizationId;
    private String clientSecret;
    public GuestToken(Activity Act, String host, PostGet.completionHandler completionHandler){
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
                    cc.put("clientSecret", clientSecret);
                    JSONObject aa = doPost(host + PostGet.urls[auth.ordinal()], cc, null);
                    if (aa.has("success") && aa.getBoolean("success") == false) {
                        sendError(aa);
                    }

                    response res = new responseBuilder().setResponseCode(200)
                            .setJsonData(aa.toString())
                            .setExceptionData(null)
                            .setResult(response.HttpSuccess.SUCCESS)
                            .setJsonObject(aa)
                            .createResponse();
                    Act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CompletionHandler.onHttpFinished(res);
                        }
                    });


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

}
