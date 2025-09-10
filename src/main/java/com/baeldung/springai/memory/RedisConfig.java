package com.baeldung.springai.memory;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.search.Schema;


@Configuration
public class RedisConfig {

    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled("localhost", 6379);
    }

    @Bean
    public VectorStore redisVectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("spring-ai-index")
                .prefix("chat:")
                .vectorAlgorithm(RedisVectorStore.Algorithm.HSNW)
                .initializeSchema(true)
                .metadataFields(new RedisVectorStore.MetadataField("response", Schema.FieldType.TEXT))
                .build();
    }
}
