package emil.dobrev.services.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static emil.dobrev.services.constant.Constants.APPOINTMENT;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic appointmentTopic() {
        return TopicBuilder.name(APPOINTMENT)
                .build();
    }
}
