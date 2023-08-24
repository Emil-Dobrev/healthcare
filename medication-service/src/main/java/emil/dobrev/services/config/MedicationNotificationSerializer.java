package emil.dobrev.services.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import emil.dobrev.services.dto.MedicationNotification;
import org.apache.kafka.common.serialization.Serializer;

public class MedicationNotificationSerializer implements Serializer<MedicationNotification> {

    private final ObjectMapper objectMapper =  new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public byte[] serialize(String s, MedicationNotification data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MedicationNotification", e);
        }
    }
}
