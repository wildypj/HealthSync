package com.wildy.doctor.officeAddress;

import org.springframework.stereotype.Service;

@Service
public interface OfficeAddressService {

    boolean addDoctorOffice(Long doctorId ,OfficeAddress officeAddress);

    boolean updateOfficeAddress(Long doctorId, Long updateOfficeId, OfficeAddress updateOfficeAddress);

    boolean deleteDoctorOffice(Long doctorId,Long officeId);
}
