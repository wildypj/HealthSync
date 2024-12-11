package com.wildy.doctor.officeAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeAddressRepository extends JpaRepository<OfficeAddress, Long> {
}
