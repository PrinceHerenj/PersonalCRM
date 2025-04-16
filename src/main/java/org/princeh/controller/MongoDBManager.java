package org.princeh.controller;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBManager {
    private static MongoDBManager mongoDBManager;
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;

    private static final String DB_NAME = "personal_crm";

    private MongoDBManager(String connectionString) {
        try {
            ConnectionString conString = new ConnectionString(connectionString);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(conString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

            mongoClient = MongoClients.create(settings);
            mongoDatabase = mongoClient.getDatabase(DB_NAME);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to connect to MongoDB",e);
        }
    }

    public static synchronized MongoDBManager getInstance(String connectionString) {
        if (mongoDBManager == null) {
            mongoDBManager = new MongoDBManager(connectionString);
        }
        return mongoDBManager;
    }

    public MongoDatabase getDatabase() {
        return mongoDatabase;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDBManager closed");
        }
    }

}

