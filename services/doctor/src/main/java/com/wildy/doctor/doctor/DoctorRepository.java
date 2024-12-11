package com.wildy.doctor.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findDoctorByEmail(String email);


    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Doctor> findBySpecializationLike(@Param("query") String query);


    // Find doctors by name with partial matching
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstname) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(d.lastname) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Doctor> findByNameLike(@Param("query") String query);
}

// Find doctors by specialization using LIKE for partial matches
//    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specialization) LIKE LOWER(CONCAT('%', ?1, '%'))")
//    List<Doctor> findBySpecializationLike(String query);

//    // #2
//    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%'))")
//    List<Doctor> findBySpecializationLike(@Param("query") String query);