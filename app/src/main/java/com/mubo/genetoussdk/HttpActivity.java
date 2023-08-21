package com.mubo.genetoussdk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mubo.genetous_http.GuestTokenBuilder;
import com.mubo.genetous_http.KillBuilder;
import com.mubo.genetous_http.LoginBuilder;
import com.mubo.genetous_http.PostGet;
import com.mubo.genetous_http.PostGetBuilder;
import com.mubo.genetous_http.VerifyBuilder;
import com.mubo.genetous_http.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** @noinspection ALL*/
public class HttpActivity extends AppCompatActivity {
    ListView lw;
    ArrayList<String> post_items=new ArrayList<>();
    String token="";
    String host="http://192.168.0.20/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        post_items.add("GetGuestToken");
        post_items.add("Login");
        post_items.add("Verify Token");
        post_items.add("Kill Token");
        post_items.add("Add Collection");
        post_items.add("Add Unique Collection");
        post_items.add("Add Relation");
        post_items.add("Get Relations");
        post_items.add("Get Collections");
        post_items.add("Update Collections");
        post_items.add("Delete Collections");
        post_items.add("Is Unique");
        post_items.add("Create Secure Link");
        post_items.add("Upload File");
        post_items.add("Get File List");
        post_items.add("Delete Files");
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, post_items);
        lw = findViewById(R.id.lw);
        lw.setAdapter(itemsAdapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        GetGuestToken();
                        break;
                     case 1:
                        Login();
                        break;
                    case 2:
                        Verify();
                        break;
                    case 3:
                        Kill();
                        break;
                    case 4:
                        try {
                            addCollection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            addUniqueCollection();;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 6:
                        try {
                            addRelation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 7:
                        try {
                            get_relation();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 8:
                        try {
                            get_collection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 9:
                        try {
                            update_collection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 10:
                        try {
                            delete_collection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 11:
                        try {
                            isUnique();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 12:
                        try {
                            createSecureLink();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 13:
                        try {
                            uploadFile();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 14:
                        getFileList();
                        break;
                    case 15:
                        try {
                            deleteFiles();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

    }
    //Guest Token : It is the minimum requirement for making requests to services.
    // You must get at least Guest Tokens. Otherwise, you will get a "401" error from the services.
    // If you don't set "Permissions" via Manage Apps. You can perform all transactions with the Guest Token.
    //Note: It is recommended to perform role-based "Permission operations" via Manage Apps

    //Guest Token : Servislere istek atabilmek için gereken minimum gereksinimdir.
    //En az Guest Token almalısınız. Yoksa servislerden "401" hatası alırsınız.
    //Manage Apps üzerinden "Permissions" ayarlarını yapmazsanız. Guest Token ile bütün işlemleri gerçekleştirebilirsiniz.
    //Not: Manage Apps üzerinden role tabanlı "Permission işlemlerini gerçekleştirmeniz önerilir"
    void GetGuestToken(){
        new GuestTokenBuilder()
                .setActivity(this)
                .setHost(host)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        try {
                            if(response.getResponseCode()==200) {
                                token = j.getString("token");
                                Toast.makeText(HttpActivity.this, token, Toast.LENGTH_SHORT).show();
                            }else{
                                String message=response.getExceptionData();
                                Toast.makeText(HttpActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .getToken()
                .process();
    }


    //Login: It is the abbreviation of the whole process that allows you to get tokens through the user collection.
    //In this transaction, it gets "Guest Token" first. It then queries the user with this token.
    //It creates a "User Token" with the last found user and gives it to you as a result output.

    //Login : User collection üzerinden token almanızı sağlayan işlemler bütününün kısaltılmış halidir.
    //Bu işlemde önce "Guest Token" alır. Daha sonra bu token ile kullanıcıyı sorgular.
    // En son bulunan kullanıcı ile "User Token" oluşturur ve sonuç çıktısı olarak size verir.
    void Login(){
        JSONObject data = new JSONObject();
        JSONObject where = new JSONObject();
        JSONObject and = new JSONObject();
        try {
            and.put("username","genetousUser");
            and.put("userpass","12345678");
            where.put("and",and);
            data.put("where",where);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new LoginBuilder()
                .setActivity(this)
                .setData(data)
                .setHost(host)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j=response.getJsonObject();
                        try {
                            if(response.getResponseCode()==200) {
                                token = j.getString("token");
                                Toast.makeText(HttpActivity.this, token, Toast.LENGTH_SHORT).show();
                            }else{
                                String message=response.getExceptionData();
                                Toast.makeText(HttpActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .createLogin()
                .process();
    }

    //Verify : Checks whether the current token is valid.
    //It returns you a result output with the "success" field.

    //Verify : Mevcut tokenın geçerli olup olmadığını kontrol eder.
    //Size "success" field'ı ile sonuç çıktısı döndürür.
    void Verify(){
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
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
                                boolean success = j.getBoolean("success");
                                Toast.makeText(HttpActivity.this, "Success : "+success, Toast.LENGTH_SHORT).show();
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

    //Kill : Allows the current token to expire.
    //It returns you a result output with the "success" field.

    //Kill : Mevcut tokenın geçerliliğinin sonlandırılmasını sağlar.
    //Size "success" field ı ile sonuç çıktısı döndürür.
    void Kill(){
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        new KillBuilder()
                .setActivity(this)
                .setToken(token)
                .setHost(host)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        if(response.getResult() == com.mubo.genetous_http.response.HttpSuccess.SUCCESS) {
                            JSONObject j = response.getJsonObject();
                            try {
                                boolean success = j.getBoolean("success");
                                Toast.makeText(HttpActivity.this, "Success : "+success, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(HttpActivity.this, response.getExceptionData(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .killToken()
                .process();
    }

    //addCollection : It simplifies the process of adding a Collection.
    //Returns the result of the operation in the onHttpFinished event.

    //addCollection : Collection ekleme işlemini kolaylaştırır.
    //İşlemin sonucunu onHttpFinished olayı içerisinde size döndürür.
    void addCollection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
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
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }

    //addUniqueCollection : It simplifies adding Unique Collection.
    //Returns the result of the operation in the onHttpFinished event.

    //addUniqueCollection : Unique Collection ekleme işlemini kolaylaştırır.
    //İşlemin sonucunu onHttpFinished olayı içerisinde size döndürür.
    void addUniqueCollection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject sendObject=new JSONObject();
        sendObject.put("collectionName","test2");

        JSONObject content = new JSONObject();
        content.put("data1","val6");
        content.put("data2","val7");
        content.put("data3","val5");
        JSONArray uniqueFields=new JSONArray();
        uniqueFields.put("data1");
        uniqueFields.put("data2");
        content.put("uniqueFields",uniqueFields);
        sendObject.put("content",content);

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken("Bearer "+token)
                .setJsonPostData(sendObject.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.addUniqueCollection)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
                .createPost()
                .process();
    }

    //addRelation: Makes adding Relation easier.
    //Returns the result of the operation in the onHttpFinished event.

    //addRelation : Relation ekleme işlemini kolaylaştırır.
    //İşlemin sonucunu onHttpFinished olayı içerisinde size döndürür.
    void addRelation() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject sendObject = new JSONObject();
        JSONArray j_relations_1 = new JSONArray();
        JSONObject jo_relations_2 = new JSONObject();
        jo_relations_2.put("relationName","rnm");
        jo_relations_2.put("id","64d580ef8128af7a55831298");
        j_relations_1.put(jo_relations_2);
        sendObject.put("relations",j_relations_1);
        JSONArray j_contents_2 = new JSONArray();
        JSONObject jo_contents_3 = new JSONObject();
        jo_contents_3.put("id","64d581df8128af7a5583129d");
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
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }

    //Get Relation : It simplifies the Relation filtering process. A simple example is made below.
    //For deeper examples, we recommend visiting https://genetous.com/documents/#/en/getstarted

    //Get Relation : Relation filtreleme işlemini kolaylaştırır. Aşağıda basit bir örnek yapılmıştır.
    //Daha derin örneklemeler için https://genetous.com/documents/#/tr/getstarted adresini ziyaret etmenizi öneririz.
    void get_relation() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject main=new JSONObject();
        JSONObject where=new JSONObject();
        JSONObject and=new JSONObject();
        JSONObject related=new JSONObject();
        JSONObject mainSort=new JSONObject();
        JSONObject relatedSort=new JSONObject();
        and.put("relationName","categoryRelations");

        related.put("collectionName","product");
        related.put("from",0);
        related.put("size",1);
        related.put("collectionName","product");
        relatedSort.put("productName","desc");
        related.put("sort",relatedSort);

        where.put("and",and);

        mainSort.put("categoryName","asc");

        main.put("related",related);
        main.put("where",where);
        main.put("from",0);
        main.put("size",1);
        main.put("sort",mainSort);

       /* {
            "where": {
            "and": {
                "relationName": "categoryProductRelation"
            }
        },
            "related": {
            "collectionName": "product",
                    "from": 0,
                    "size": 1,
                    "sort": {
                "productName": "desc"
            }
        },
            "from": 0,
                "size": 1,
                "sort": {
            "categoryName": "asc"
        }
        }*/

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(main.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.getRelations)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                        //Use Json Object
                    }
                })
                .createPost()
                .process();
    }

    //Get Collection: It simplifies the collection filtering process. A simple example is made below.
    //For deeper examples, we recommend visiting https://genetous.com/documents/#/en/getstarted

    //Get Collection: Collection filtreleme işlemini kolaylaştırır. Aşağıda basit bir örnek yapılmıştır.
    //Daha derin örneklemeler için https://genetous.com/documents/#/tr/getstarted adresini ziyaret etmenizi öneririz.
    void get_collection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject data = new JSONObject();
        JSONObject where = new JSONObject();
        JSONObject and = new JSONObject();
        try {
            and.put("username","genetousUserd");
            where.put("and",and);
            data.put("where",where);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(data.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.getCollections)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }

    //Update Collection: It simplifies the process of updating collections. A simple example is made below.
    //For deeper examples, we recommend visiting https://genetous.com/documents/#/en/getstarted.

    //Update Collection: Collectionları güncelleme işlemini kolaylaştırır. Aşağıda basit bir örnek yapılmıştır.
    //Daha derin örneklemeler için https://genetous.com/documents/#/en/getstarted adresini ziyaret etmenizi öneririz.
    void update_collection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject data = new JSONObject();
        JSONObject where = new JSONObject();
        JSONObject and = new JSONObject();
        JSONArray fields=new JSONArray();
        JSONObject field=new JSONObject();
        try {
            and.put("productName","X Shoes");
            where.put("and",and);

            field.put("field","productPrice");
            field.put("value","110");

            fields.put(field);

            data.put("where",where);
            data.put("fields",fields);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(data.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.updateCollection)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }

    //Delete Collection: It simplifies the process of deleting collections. A simple example is made below.
    //For deeper examples, we recommend visiting https://genetous.com/documents/#/en/getstarted.

    //Delete Collection: Collectionları silme işlemini kolaylaştırır. Aşağıda basit bir örnek yapılmıştır.
    //Daha derin örneklemeler için https://genetous.com/documents/#/en/getstarted adresini ziyaret etmenizi öneririz.
    void delete_collection() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject data = new JSONObject();
        JSONObject where = new JSONObject();
        JSONObject and = new JSONObject();
        try {
            and.put("categoryName","Sub 1");
            where.put("and",and);
            data.put("where",where);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(data.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.deleteCollection)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }

    //IsUnique : Allows you to check whether a field is unique in a collection.

    //IsUnique : Bir collection içerisinde bir alanın unique olup olmadığını kontrol etmenizi sağlar.
    void isUnique() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject data = new JSONObject();
        JSONObject where = new JSONObject();
        JSONObject and = new JSONObject();
        try {
            and.put("collectionName","user");
            and.put("username","genetousUserd");
            where.put("and",and);
            data.put("where",where);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(data.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.isUnique)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }

    //CreateSecureLink : Makes it easy to create a Secure Link for the update process.
    //Example : When a new user is registered,
    //you do not activate the user until that user verifies its e-mail address.
    // You create and send a Secure Link with this method for the user to verify their e-mail address.

    //CreateSecureLink : Update işlemi için Secure Link oluşturma işlemi gerçekleştirmenizi kolaylaştırır.
    //Örnek : Yeni kullanıcı kaydı gerçekleştiğinde,
    // O kullanıcı E-Posta adresini doğrulayana kadar kullanıcıyı aktif etmezsiniz.
    // Kullanıcının E-Posta adresini doğrulaması için bu metot ile Secure Link oluşturur ve gönderirsiniz.
    void createSecureLink() throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject data = new JSONObject();
        try {
            data.put("collectionName","user");
            data.put("usermail","genetous@genetous.com");
            data.put("update","userpass");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(data.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.CreateSecureLink)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }
    void uploadFile() throws JSONException {
       if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, 123);

    }
    void upload(Uri f){
        Map<String, Object> parameters=new HashMap<>();
        parameters.put("random",true);

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setPost_type(PostGet.POST_TYPE.MULTIPART)
                .setPostFile(f)
                .setToken(token)
                .setParameters(parameters)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.uploadFile)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            try {
                                j = new JSONObject(response.getData());
                                Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                                Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(HttpActivity.this, response.getExceptionData(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                })
                .createPost()
                .process();
    }

    void getFileList(){
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.GET)
                .setToken(token)
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.getFileList)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(j.toString());
                    }
                })
                .createPost()
                .process();
    }
    void deleteFiles()  throws JSONException {
        if(token==""){
            Toast.makeText(HttpActivity.this, "Lütfen önce login işlemi yapın veya Guest Token Alın.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray files = new JSONArray();
        files.put("1rvpq2og3ggluryb.jpg");
        JSONObject data =new JSONObject();
        data.put("files",files);

        new PostGetBuilder()
                .setActivity(this)
                .setMethod(PostGet.REQUEST_METHODS.POST)
                .setToken(token)
                .setJsonPostData(data.toString())
                .setReturn_type(PostGet.RETURN_TYPE.JSONOBJECT)
                .setHost(host)
                .setUrlType(PostGet.URL_TYPE.deleteFile)
                .setCompletionHandler(new PostGet.completionHandler() {
                    @Override
                    public void onHttpFinished(response response) {
                        JSONObject j;
                        if(response.getResponseCode()==200) {
                            j = response.getJsonObject();
                            Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                j = new JSONObject(response.getExceptionData());
                                Toast.makeText(HttpActivity.this, j.toString(), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(HttpActivity.this, response.getExceptionData().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                })
                .createPost()
                .process();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            upload(uri);
        }
    }
}