package emil.dobrev.services.service.interfaces;

import emil.dobrev.services.model.AppointmentNotification;

public interface EmailService {

    void sendEmail(EmailMetaInformation emailMetaInformation);

    EmailMetaInformation buildEmailMetaInformation(AppointmentNotification appointmentNotification);
}
