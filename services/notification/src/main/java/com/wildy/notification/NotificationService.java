package com.wildy.notification;

import com.wildy.notification.clients.DTO.PatientDTO;
import com.wildy.notification.clients.PatientFeignClient;
import com.wildy.notification.email.EmailService;
import com.wildy.notification.payload.AppointmentEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final PatientFeignClient patientFeignClient;
    private final EmailService emailService;

    public void triggerNotification(AppointmentEventDTO event) {
        try {
            // Fetch patient details using Feign client
            PatientDTO patient = patientFeignClient.getPatientById(event.getPatientId());

            // Send email notification
            String subject = "Appointment " + event.getAppointmentType();
            String messageBody = buildEmailMessage(event, patient);
            emailService.sendEmail(patient.getEmail(), subject, messageBody);

            log.info("Email sent to Patient: {} {} about Appointment [{}] with status: {}",
                    patient.getFirstname(), patient.getLastname(), event.getAppointmentType(), event.getStatus());

        } catch (Exception e) {
            log.error("Failed to send email notification for appointment event: {}", event, e);
        }
    }

    private String buildEmailMessage(AppointmentEventDTO event, PatientDTO patient) {
        String greeting = "Dear " + patient.getFirstname() + " " + patient.getLastname() + ",";
        String statusMessage;

        switch (event.getAppointmentType()) {
            case "Created" -> statusMessage = "Your appointment has been successfully created.";
            case "Updated" -> statusMessage = "Your appointment has been updated.";
            case "Canceled" -> statusMessage = "Your appointment has been canceled.";
            default -> statusMessage = "There has been an update to your appointment.";
        }

        return greeting + "\n\n" + statusMessage + "\nStatus: " + event.getStatus() + "\n\nThank you,\nHealthSync";
    }

}

//Maybe add later

//public void sendSmsNotification(AppointmentEventDTO event) {
//    // call doctor and patient service with open feign by Id
//    // make sure to get Patient numbers and send notification
//
//    // Simulate sending an SMS notification
//    log.info("Sending SMS to Patient ID: {} about Appointment [{}] with status: {}",
//            event.getPatientId(), event.getAppointmentType(), event.getStatus());
//}