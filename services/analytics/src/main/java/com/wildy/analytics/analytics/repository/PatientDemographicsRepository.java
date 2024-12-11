package com.wildy.analytics.analytics.repository;

import com.wildy.analytics.analytics.entity.PatientDemographics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface PatientDemographicsRepository extends JpaRepository<PatientDemographics, Long> {
    @Query("SELECT d.gender, COUNT(d) FROM PatientDemographics d GROUP BY d.gender")
    Map<String, Long> countByGender();

    @Query("SELECT d.age, COUNT(d) FROM PatientDemographics d GROUP BY d.age")
    Map<Integer, Long> countByAgeGroup();
}
