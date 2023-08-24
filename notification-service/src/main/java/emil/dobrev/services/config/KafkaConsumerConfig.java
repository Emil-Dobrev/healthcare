package emil.dobrev.services.config;

import emil.dobrev.services.model.AppointmentNotification;
import emil.dobrev.services.model.MedicationNotification;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Bean
    public ConsumerFactory<String, Object> appointmentConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "appointment-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, String.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AppointmentNotificationDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

//    @Bean
//    public ConsumerFactory<String, AppointmentNotification> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(appointmentConsumerConfig());
//    }

    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, AppointmentNotification>> appointmentFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AppointmentNotification> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(appointmentConsumerConfig());
        return factory;
    }


    public ConsumerFactory<String, Object> medicationConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "medication-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, String.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MedicationtNotificationDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, MedicationNotification>> medicationFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MedicationNotification> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(medicationConsumerFactory());
        return factory;
    }

}
