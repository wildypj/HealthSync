package com.wildy.doctor.schedule;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wildy.doctor.doctor.Doctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    @PrePersist
    @PreUpdate
    public void validateSchedule() {
        if (endTime.isBefore(startTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        long duration = java.time.Duration.between(startTime, endTime).toMinutes();
        if (duration < 45 || duration > 60) {
            throw new IllegalArgumentException("Schedule duration must be between 45 and 60 minutes.");
        }
    }
}
