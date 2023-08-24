package emil.dobrev.services.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import emil.dobrev.services.model.AppointmentNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

@RequiredArgsConstructor
@Slf4j
public class AppointmentNotificationDeserializer implements Deserializer<AppointmentNotification> {

    private final ObjectMapper objectMapper =  new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public AppointmentNotification deserialize(String s, byte[] data) {
        try {
            if (data == null){
                log.error("Null received at deserializing");
                return null;
            }
            log.error("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), AppointmentNotification.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }
}
