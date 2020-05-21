package com.uxpsystems.assignment.profileservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.uxpsystems.assignment.profileservice.constant.ProfileConstants;
import com.uxpsystems.assignment.profileservice.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ConsumerKafkaConfiguration {
	
	@Value("${bootstrap.server.url}")
	private String bootstrapServer;

	//consumer configuration
    @Bean
    public ConsumerFactory<String, UserProfile> userConsumerFactory() {
    	
        //allow deserializer in config
    	JsonDeserializer<UserProfile> deserializer = new JsonDeserializer<>(UserProfile.class);
	    deserializer.setRemoveTypeHeaders(false);
	    deserializer.addTrustedPackages("*");
	    deserializer.setUseTypeMapperForKey(true);
	    
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, ProfileConstants.GROUP_JSON);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
        		deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserProfile> profileKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserProfile> factory = new ConcurrentKafkaListenerContainerFactory<String, UserProfile>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }
    
}
