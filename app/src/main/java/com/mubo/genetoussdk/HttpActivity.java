package com.mubo.genetoussdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mubo.genetous_http.LoginBuilder;
import com.mubo.genetous_http.PostGet;
import com.mubo.genetous_http.PostGetBuilder;
import com.mubo.genetous_http.VerifyBuilder;
import com.mubo.genetous_http.response;
import com.mubo.genetoussdk.adapter.PostListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HttpActivity extends AppCompatActivity {
    ListView lw;
    PostListAdapter pa;
    ArrayList<String> post_items=new ArrayList<>();
    String token="";
    String host="ip address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        post_items.add("Login");
        post_items.add("Verify Token");
        post_items.add("Add Collection");
        post_items.add("Add Relation");
        post_items.add("Get Relations");
        post_items.add("Get Collections");
        pa = new PostListAdapter(this, post_items);
        lw = findViewById(R.id.lw);
        lw.setAdapter(pa);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Login();
                        break;
                    case 1:
                        Verify();
                        break;
                    case 2:
                        try {
                            addCollection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            addRelation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        try {
                            get_relation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            get_collection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

    }
    void Login(){
        JSONObject sendObject = new JSONObject();
        try {
            sendObject.put("user_username","demo");
            sendObject.put("user_password","demo");
            JSONArray ja=new JSONArray();
            ja.put("user_password");
            sendObject.put("remove_fields",ja);
            sendObject.put("type","login");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new LoginBuilder()
                .setActivity(this)
                .setData(sendObject)
                .setHost(host)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        try {
                            token=j.getString("token");
                            Toast.makeText(HttpActivity.this, token, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .createLogin()
                .process();
    }
    void Verify(){
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın.", Toast.LENGTH_SHORT).show();
            return;
        }
        new VerifyBuilder()
                .setActivity(this)
                .setToken(token)
                .setHost(host)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        if(response.getResult() == com.mubo.genetous_http.response.HttpSuccess.SUCCESS) {
                            JSONObject j = response.getJsonObject();
                            try {
                                String clientId = j.getString("clientId");
                                Toast.makeText(HttpActivity.this, clientId, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(HttpActivity.this, response.getExceptionData(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .createVerify()
                .process();
    }
    void addCollection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject sendObject=new JSONObject();
        sendObject.put("collectionName","test2");

        JSONObject content = new JSONObject();
        content.put("data1","val1");
        content.put("data2","val2");
        content.put("data3","val5");
        sendObject.put("content",content);

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(sendObject.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.addCollection)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        //Use Json Object
                    }
                })
                .createPost()
                .process();
    }
    void addRelation() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject sendObject = new JSONObject();
        JSONArray j_relations_1 = new JSONArray();
        JSONObject jo_relations_2 = new JSONObject();
        jo_relations_2.put("relationName","");
        jo_relations_2.put("id","61f6ccf1afc55a5bc7d6500e");
        j_relations_1.put(jo_relations_2);
        sendObject.put("relations",j_relations_1);
        JSONArray j_contents_2 = new JSONArray();
        JSONObject jo_contents_3 = new JSONObject();
        jo_contents_3.put("id","61ba643aa6689921e7ad4725");
        j_contents_2.put(jo_contents_3);
        sendObject.put("contents",j_contents_2);
        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(sendObject.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.addRelation)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        //Use Json Object
                    }
                })
                .createPost()
                .process();
    }

    void get_relation() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject sendObject=new JSONObject();
        sendObject.put("content.test2.content.test","tt2");

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(sendObject.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.getRelations)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        //Use Json Object
                    }
                })
                .createPost()
                .process();
    }
    void get_collection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject sendObject=new JSONObject();
        sendObject.put("collectionName","user");

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(sendObject.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.getCollections)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        //Use Json Object
                    }
                })
                .createPost()
                .process();
    }
}