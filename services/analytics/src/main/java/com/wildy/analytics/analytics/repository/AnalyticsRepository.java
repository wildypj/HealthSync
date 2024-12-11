package com.wildy.analytics.analytics.repository;

import com.wildy.analytics.analytics.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AnalyticsRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a.specialty, COUNT(a) FROM Appointment a GROUP BY a.specialty")
    Map<String, Long> countBySpecialty();

    @Query("SELECT COUNT(DISTINCT a.patientId) FROM Appointment a")
    long countDistinctPatientIds();

    @Query("SELECT COUNT(DISTINCT a.patientId) FROM Appointment a GROUP BY a.patientId HAVING COUNT(a) > 1")
    long countRepeatPatientIds();
}
