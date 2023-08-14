package emil.dobrev.services.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import emil.dobrev.services.model.AppointmentNotification;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

@RequiredArgsConstructor
public class AppointmentNotificationDeserializer implements Deserializer<AppointmentNotification> {

    private final ObjectMapper objectMapper =  new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public AppointmentNotification deserialize(String s, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), AppointmentNotification.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to MessageDto");
        }
    }
}
