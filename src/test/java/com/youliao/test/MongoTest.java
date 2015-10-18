package com.youliao.test;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest {
	public static void main(String[] args) {
		try {
			// 连接到 mongodb 服务
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			// 连接到数据库
			MongoDatabase db = mongoClient.getDatabase("nature");
			System.out.println("Connect to database successfully");
			// boolean auth = db.authenticate(myUserName, myPassword);
			// System.out.println("Authentication: "+auth);
			MongoCollection<Document> coll = db.getCollection("mycol");
			System.out.println("Collection mycol selected successfully");
			/*BasicDBObject doc = new BasicDBObject("title", "MongoDB")
					.append("description", "database").append("likes", 100)
					.append("url", "http://www.w3cschool.cc/mongodb/")
					.append("by", "w3cschool.cc");*/
			
		/*	OnlineInfoDocument docx=new OnlineInfoDocument();
			docx.setNickname("ok");
			docx.setCity("chengdu");
			String json=JSONUtils.toJSONString(docx);*/
			Document document=new Document();
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("name", "guanxf");
			map.put("age", 18);
			document.putAll(map);
			coll.insertOne(document);
			System.out.println("Document inserted successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
