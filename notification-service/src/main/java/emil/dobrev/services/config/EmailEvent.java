package emil.dobrev.services.config;

import emil.dobrev.services.model.Notification;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
@EqualsAndHashCode(callSuper = true)
public class EmailEvent<T> extends ApplicationEvent {

    Notification notification;

    public EmailEvent(Object source, Notification notification) {
        super(source);
        this.notification = notification;
    }
}
