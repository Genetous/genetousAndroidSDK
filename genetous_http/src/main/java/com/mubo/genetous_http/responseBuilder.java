package com.mubo.genetous_http;

import org.json.JSONArray;
import org.json.JSONObject;

public class responseBuilder {
    private int responseCode;
    private String jsonData;
    private Object mapperData;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String exceptionData;
    private int requestCode;
    private response.HttpSuccess result;

    public responseBuilder setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public responseBuilder setJsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    public responseBuilder setMapperData(Object mapperData) {
        this.mapperData = mapperData;
        return this;
    }

    public responseBuilder setExceptionData(String exceptionData) {
        this.exceptionData = exceptionData;
        return this;
    }

    public responseBuilder setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public responseBuilder setResult(response.HttpSuccess result) {
        this.result = result;
        return this;
    }

    public responseBuilder setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public responseBuilder setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        return this;
    }

    public response createResponse() {
        return new response(responseCode, jsonData, mapperData, exceptionData, requestCode, result,jsonObject,jsonArray);
    }
}
