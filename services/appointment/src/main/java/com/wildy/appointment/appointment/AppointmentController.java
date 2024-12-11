package com.wildy.appointment.appointment;

import com.wildy.appointment.appointment.payload.AppointmentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Retrieve appointments by doctor, patient, and date.
    // get All appointments
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id){
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    // Create a new appointment.
    @PostMapping
    public ResponseEntity<String> createAppointment(
            @RequestBody AppointmentDTO appointmentDTO) {
        try {
            appointmentService.createAppointment(appointmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Appointment created successfully");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // update appointment
    @PutMapping("/{appointmentId}")
    public ResponseEntity<String> updateAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentDTO updatedAppointmentDTO) {
        try{
            appointmentService.updateAppointment(appointmentId,updatedAppointmentDTO);
            return ResponseEntity.ok("Appointment updated successfully");
        }catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cancel appointment
    @DeleteMapping("{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long appointmentId) {
        try{
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok("Appointment Canceled successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
