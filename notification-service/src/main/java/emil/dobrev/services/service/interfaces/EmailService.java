package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.model.AppointmentNotification;

public interface EmailService {

    <T> void sendEmail(EmailMetaInformation emailMetaInformation, T object);

    EmailMetaInformation buildEmailMetaInformation(AppointmentNotification appointmentNotification);
}
