package com.fr.plugin.db.mongodb.connect.core.handler.bean;

import com.fr.plugin.db.mongodb.connect.core.handler.bean.emb.MongoDB;
import com.fr.plugin.db.mongodb.table.util.MongoDBDataDealUtils;
import com.fr.plugin.db.mongodb.table.util.MongoDBUtils;
import com.fr.plugin.db.mongodb.table.data.DataWarp;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

public class SimpleMongoDBClient implements MongoDB{
    //连接到mongodb
    MongoClient mongoClient;
    //指定数据库
    MongoDatabase mongoDatabase;

    public void noPwd(String host,String port,String database){
        try{
            //System.out.println("======SimpleMongoDBClient nopwd Create Start==========");
            //System.out.println("host:"+host);
            //System.out.println("port:"+port);
            //System.out.println(getUsername());
            //System.out.println(getPassword());
            //System.out.println("database:"+database);
            mongoClient=new MongoClient(host,Integer.parseInt(port));

            mongoDatabase=mongoClient.getDatabase(database);

            MongoCollection<Document> t1 = mongoDatabase.getCollection("t1");
            MongoCursor<Document> iterator = t1.find().iterator();

            while (iterator.hasNext()){
                Document next = iterator.next();
                //System.out.println(next);
            }
            //System.out.println("======SimpleMongoDBClient nopwd Create End==========\n");


        }catch (Exception e){
            //System.out.println(e);
            //System.out.println("======SimpleMongoDBClient nopwd Create End==========\n");

        }

    }

    public void usePwd(String host, String port, String username, String password, String database){
        try {
            //System.out.println("======SimpleMongoDBClient Create whitpwd Start==========");
            //System.out.println("host:"+host);
            //System.out.println("port:"+port);
            //System.out.println("host:"+host);
            //System.out.println("port:"+port);
            //System.out.println("username:"+username);
            //System.out.println("password:"+password);
            //System.out.println("database:"+database);

            ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
            MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());

            mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
            mongoDatabase = mongoClient.getDatabase(database);

            //System.out.println("======SimpleMongoDBClient Create whitpwd End==========\n");

        }catch (Exception e){
            //System.out.println(e);
            //System.out.println("======SimpleMongoDBClient Create whitpwd End==========\n");

        }

    }


    @Override
    public String test() {
        try {
            String s = mongoClient.getConnectPoint();
            //System.out.println("test result:success->"+s);

            return s;
        }catch (Exception e){
            //System.out.println("test result:false");

            //System.out.println(e);

            return "false";
        }

    }

    @Override
    public DataWarp find(String query) {
        mongoDatabase=mongoClient.getDatabase(MongoDBUtils.database);
        String[] strings=query.split("\\.");
        //db.t1.find();
        String doc=strings[1];
        String action=strings[2];
        MongoCollection<Document> collection = mongoDatabase.getCollection(doc);
        MongoCursor<Document> results = collection.find().iterator();

        //处理结果集合,得到列名
        DataWarp<Object> dataWarpper = MongoDBDataDealUtils.getDataWarpper(results);

        return dataWarpper;
    }

    @Override
    public void close() {
        //mongoClient.close();
    }

}
