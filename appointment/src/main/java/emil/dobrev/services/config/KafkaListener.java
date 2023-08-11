package emil.dobrev.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListener {

    @org.springframework.kafka.annotation.KafkaListener(
            topics = "appointment",
            groupId = "my-group"
    )
    void listener(String data) {
        System.out.println(data);
        log.error(data);
        log.error(data);
        log.error(data);
        log.error(data);
    }
}
