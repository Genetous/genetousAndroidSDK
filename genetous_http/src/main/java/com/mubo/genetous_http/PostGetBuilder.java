package com.mubo.genetous_http;

import android.app.Activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PostGetBuilder {
    private PostGet.URL_TYPE url_type;
    private String host;
    private String jsonPostData;
    private Map<String, String> parameters;
    private PostGet.REQUEST_METHODS method;
    private PostGet.POST_TYPE post_type;
    private String token;
    private PostGet.RETURN_TYPE return_type;
    private Class mapperClass;
    private PostGet.completionHandler completionHandler;
    private int requestCode;
    private File postFile;
    private Activity act;

    public PostGetBuilder setUrlType(PostGet.URL_TYPE url_type) {
        this.url_type = url_type;
        return this;
    }
    public PostGetBuilder setHost(String host){
        this.host=host;
        return this;
    }
    public PostGetBuilder setJsonPostData(String jsonPostData) {
        this.jsonPostData = jsonPostData;
        return this;
    }

    public PostGetBuilder setParameters(String key, String value) {
        if(parameters==null)
            parameters = new HashMap<>();
        parameters.put(key,value);
        return this;
    }

    public PostGetBuilder setMethod(PostGet.REQUEST_METHODS method) {
        this.method = method;
        return this;
    }

    public PostGetBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public PostGetBuilder setReturn_type(PostGet.RETURN_TYPE return_type) {
        this.return_type = return_type;
        return this;
    }
    public PostGetBuilder setPost_type(PostGet.POST_TYPE post_type) {
        this.post_type = post_type;
        return this;
    }

    public PostGetBuilder setMapperClass(Class mapperClass) {
        this.mapperClass = mapperClass;
        return this;
    }

    public PostGetBuilder setCompletionHandler(PostGet.completionHandler completionHandler) {
        this.completionHandler = completionHandler;
        return this;
    }

    public PostGetBuilder setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public PostGetBuilder setPostFile(File postFie) {
        this.postFile = postFie;
        return this;
    }

    public PostGetBuilder setActivity(Activity act) {
        this.act = act;
        return this;
    }
    public PostGet createPost() {
        return new PostGet(host,url_type, jsonPostData, parameters, method, post_type,
                token, return_type, mapperClass, completionHandler, requestCode,postFile,act);
    }
}
