package com.mubo.genetous_http;

import org.json.JSONArray;
import org.json.JSONObject;

public class response {
    public enum HttpSuccess{
        SUCCESS,
        FAIL
    }
    private int ResponseCode;
    private String Data;
    private Object MapperData;
    private JSONObject JsonObject;
    private JSONArray JsonArray;
    private String ExceptionData;
    private int RequestCode;
    private HttpSuccess Result;

    public response(int responseCode, String data, Object mapperData, String exceptionData, int requestCode, HttpSuccess result,JSONObject jsonObject,JSONArray jsonArray) {
        ResponseCode = responseCode;
        Data = data;
        MapperData = mapperData;
        ExceptionData = exceptionData;
        RequestCode = requestCode;
        Result = result;
        JsonObject=jsonObject;
        JsonArray=jsonArray;
    }

    public int getResponseCode() {
        return ResponseCode;
    }

    public String getData() {
        return Data;
    }

    public Object getMapperData() {
        return MapperData;
    }

    public String getExceptionData() {
        return ExceptionData;
    }

    public int getRequestCode() {
        return RequestCode;
    }

    public HttpSuccess getResult() {
        return Result;
    }

    public JSONArray getJsonArray() {
        return JsonArray;
    }

    public JSONObject getJsonObject() {
        return JsonObject;
    }
}
