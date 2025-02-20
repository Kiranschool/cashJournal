package com.cashjournal.app.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "cashjournal";
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSslSettings(builder -> {
                builder.enabled(true)
                    .invalidHostNameAllowed(true);
            })
            .applyToSocketSettings(builder -> 
                builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
                      .readTimeout(60000, TimeUnit.MILLISECONDS)
            )
            .applyToClusterSettings(builder -> 
                builder.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS)
            )
            .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.cashjournal.app");
    }
} 