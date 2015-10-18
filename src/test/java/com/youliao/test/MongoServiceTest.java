package com.youliao.test;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.youliao.ms.document.management.OnlineInfoDocument;
import com.youliao.ms.repository.management.OnlineInfoRepostitory;

public class MongoServiceTest{
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring-mangodb.xml");
		try {
			OnlineInfoRepostitory mangoDbService =context.getBean(OnlineInfoRepostitory.class);
			Document document=new Document();
			/*Map<String, Object> map=new HashMap<String,Object>();
			map.put("name", "guanxf00");
			map.put("age", 18);
			document.putAll(map);*/
			document.append(OnlineInfoDocument.uid,"13540042040");
			document.append(OnlineInfoDocument.city,"成都市武侯区00-0");
			document.append(OnlineInfoDocument.constellation,"0");
			
			mangoDbService.insertOrUpdateOne(document);
//			mangoDbService.insertOne(document);
			
			/*document.append(OnlineInfoDocument.uid,"13540042045");
			mangoDbService.deleteOne(document);*/
			System.out.println("Document inserted successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
