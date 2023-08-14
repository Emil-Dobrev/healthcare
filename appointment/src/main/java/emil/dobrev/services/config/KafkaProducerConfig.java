package emil.dobrev.services.config;

import emil.dobrev.services.dto.AppointmentNotification;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    public Map<String, Object> produceConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AppointmentNotificationSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, AppointmentNotification> producerFactory() {
        return new DefaultKafkaProducerFactory<>(produceConfig());
    }

    @Bean
    public KafkaTemplate<String, AppointmentNotification> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
