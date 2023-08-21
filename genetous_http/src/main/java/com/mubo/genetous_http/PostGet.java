package com.mubo.genetous_http;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PostGet {
    public interface completionHandler {
        void onHttpFinished(response response);
    }

    public enum REQUEST_METHODS {
        POST,
        GET
    }

    public enum RETURN_TYPE {
        STRING,
        MAPPER,
        JSONOBJECT,
        JSONARRAY
    }

    public enum POST_TYPE {
        MULTIPART,
        JSON
    }

    public enum URL_TYPE {
        addUniqueCollection,
        addCollection,
        updateCollection,
        deleteCollection,
        deleteRelation,
        addRelation,
        getCollection,
        getCollections,
        getRelations,
        removeRelation,
        selectRelation,
        search,
        uploadFile,
        deleteFile,
        getFileList,
        sendMail,
        sendMobileNofication,
        sendDBJob,
        mapCollectionAnalytics,
        client,
        auth,
        verify,
        isUnique,
        CreateSecureLink,
        killToken
    }
    public static String[] urls = {
            "dataservice/add/unique/collection",
            "dataservice/add/collection",
            "dataservice/update/collection",
            "dataservice/delete/collection",
            "dataservice/delete/relation",
            "dataservice/add/relation",
            "dataservice/get/collection",
            "dataservice/get/collections",
            "dataservice/get/relations",
            "dataservice/remove/relation",
            "dataservice/select/relation",
            "dataservice/search",
            "osservice/upload",
            "osservice/delete",
            "osservice/list/objects",
            ":5001/send/mail",
            ":5001/send/notification",
            ":5003/add/dbjob",
            ":5014/map",
            "authservice/client",
            "authservice/auth",
            "authservice/verify",
            "dataservice/check/unique",
            "dataservice/create/slink",
            "authservice/logout",
    };
    private URL_TYPE Url_Type;
    private String Host;
    private String JsonPostData;
    private Map<String, Object> Parameters;
    private REQUEST_METHODS Method;
    private POST_TYPE Post_type;
    private String Token;
    private RETURN_TYPE Return_type;
    private Class MapperClass;
    private completionHandler CompletionHandler;
    private int RequestCode;
    private Uri PostFile;
    private Activity Act;
    private String applicationId;
    private String organizationId;

    public PostGet(String host, URL_TYPE url_type, String jsonPostData, Map<String, Object> parameters,
                   REQUEST_METHODS method, POST_TYPE post_type, String token,
                   RETURN_TYPE return_type, Class mapperClass, completionHandler completionHandler,
                   int requestCode, Uri postFile, Activity act) {
        Host = host;
        Url_Type = url_type;
        JsonPostData = jsonPostData;
        Parameters = parameters;
        Method = method;
        Post_type = post_type;
        Token = token;
        Return_type = return_type;
        MapperClass = mapperClass;
        CompletionHandler = completionHandler;
        RequestCode = requestCode;
        PostFile = postFile;
        Act = act;
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

    public void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Post_type == POST_TYPE.JSON)
                    JsonPost();
                else if (Post_type == POST_TYPE.MULTIPART) {
                    MultiPost();
                } else {
                    JsonPost();
                }
            }
        }).start();

    }

    private void MultiPost() {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String data = null;
        Object cData = null;
        String ExceptionData = null;
        response.HttpSuccess result;
        final response res;
        int responsCode = -1;
        JSONObject jo = null;
        JSONArray ja = null;

        if (PostFile == null) {
            result = response.HttpSuccess.FAIL;
            ExceptionData = "File not exists";
        } else {

            try {
                MultiPartUtility multipart = new MultiPartUtility(getUrl(), "UTF-8",Token);

                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");
                if (PostFile != null) {
                    multipart.addFilePart("file", PostFile,Act);
                }
                for(String k: Parameters.keySet()){
                    multipart.addFormField(k,Parameters.get(k).toString());
                }
                responsCode =  multipart.getStatus();
                data = multipart.finish();
                if(responsCode!=200) {
                    if(responsCode==500){
                        JSONObject err500=new JSONObject();
                        err500.put("success",false);
                        err500.put("message","Unexpected Error Occured!");
                        err500.put("code",responsCode);
                        ExceptionData = err500.toString();
                        result = response.HttpSuccess.FAIL;
                        sendResult(responsCode,null,null,ExceptionData,result,null,null);
                        return;
                    }
                    result = response.HttpSuccess.FAIL;
                    sendResult(responsCode,null,null,data,result,null,null);
                    return;
                }

                if (Return_type == RETURN_TYPE.MAPPER) {
                    ObjectMapper mapper = new ObjectMapper();
                    cData = mapper.readValue(data, MapperClass);
                } else if (Return_type == RETURN_TYPE.JSONOBJECT || Return_type == RETURN_TYPE.JSONARRAY) {

                    if (Return_type == RETURN_TYPE.JSONARRAY) {
                        ja = new JSONArray(data);
                    } else {
                        jo = new JSONObject(data);
                    }
                }
                result = response.HttpSuccess.SUCCESS;
                sendResult(responsCode,data,cData,null,result,jo,ja);
            } catch (IOException | JSONException e) {
                result = response.HttpSuccess.FAIL;
                ExceptionData = e.toString();
                sendResult(responsCode,null,null,ExceptionData,result,null,null);
            }
        }
    }

    private void JsonPost() {
        int responsCode = -1;
        String data = null;
        Object cData = null;
        JSONObject jo = null;
        JSONArray ja = null;
        String ExceptionData = null;
        response.HttpSuccess result;
        String m = Method == REQUEST_METHODS.POST ? "POST" : "GET";
        final response res;
        try {
            URL connectURL = new URL(getUrl());
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setRequestMethod(m);
            if (Token != null && !Token.isEmpty())
                conn.setRequestProperty("Authorization", Token);
            if (Method == REQUEST_METHODS.POST) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
                os.write(JsonPostData);
                os.flush();
            }
            responsCode = conn.getResponseCode();
            InputStream is=null;
            if(responsCode!=200) {
                if(responsCode==500){
                    JSONObject err500=new JSONObject();
                    err500.put("success",false);
                    err500.put("message","Unexpected Error Occured!");
                    err500.put("code",responsCode);
                    ExceptionData = err500.toString();
                    result = response.HttpSuccess.FAIL;
                    sendResult(responsCode,null,null,ExceptionData,result,null,null);
                    return;
                }
                is = conn.getErrorStream();
                ExceptionData = convertStreamToString(is);
                result = response.HttpSuccess.FAIL;
                sendResult(responsCode,null,null,ExceptionData,result,null,null);
                return;
            }else {
                is = conn.getInputStream();
            }
            data = convertStreamToString(is);
            if (Return_type == RETURN_TYPE.MAPPER) {
                ObjectMapper mapper = new ObjectMapper();
                cData = mapper.readValue(data, MapperClass);
            } else if (Return_type == RETURN_TYPE.JSONOBJECT || Return_type == RETURN_TYPE.JSONARRAY) {

                if (Return_type == RETURN_TYPE.JSONARRAY) {
                    ja = new JSONArray(data);
                } else {
                    jo = new JSONObject(data);
                }
            }
            result = response.HttpSuccess.SUCCESS;
            sendResult(responsCode,data,cData,null,result,jo,ja);
        } catch (Exception e) {
            result = response.HttpSuccess.FAIL;
            ExceptionData = e.toString();
            sendResult(responsCode,null,null,ExceptionData,result,null,null);
        }

    }
    void sendResult(int code,String jsonData,Object mapperData,String exceptionData,
                    response.HttpSuccess result,JSONObject jo,JSONArray ja){
       response res = new responseBuilder().setResponseCode(code)
                .setJsonData(jsonData)
                .setMapperData(mapperData)
                .setExceptionData(exceptionData)
                .setRequestCode(RequestCode)
                .setResult(result)
                .setJsonObject(jo)
                .setJsonArray(ja)
                .createResponse();
        Act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CompletionHandler.onHttpFinished(res);
            }
        });
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



    private String getUrl() {
        return Host + urls[Url_Type.ordinal()];
    }
}
