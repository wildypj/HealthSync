package com.wildy.doctor.schedule;

import com.wildy.doctor.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDoctorAndDate(Doctor doctor, LocalDate date);

    List<Schedule> findByDoctor(Doctor doctor);
}
