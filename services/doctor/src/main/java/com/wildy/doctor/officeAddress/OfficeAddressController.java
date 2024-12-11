package com.wildy.doctor.officeAddress;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{doctorId}/office-addresses")
@RequiredArgsConstructor
public class OfficeAddressController {

    private final OfficeAddressService officeAddressService;

    // create office address by
    @PostMapping
    public ResponseEntity<String> createOfficeAddress(
            @PathVariable  Long doctorId,
            @RequestBody OfficeAddress officeAddress) {
        boolean addressAdded = officeAddressService.addDoctorOffice(doctorId,officeAddress);
        return addressAdded
                ? ResponseEntity.status(HttpStatus.CREATED).body("Office successfully created")
                : ResponseEntity.badRequest()
                .body("Failed to create address. Can only Have one Office Address");
    }

    //update method for doctor
    @PutMapping("/{updateOfficeId}")
    public ResponseEntity<String> updateOfficeAddress(
            @PathVariable Long doctorId,
            @PathVariable Long updateOfficeId,
            @RequestBody OfficeAddress updateOfficeAddress
    ){
        boolean addressUpdated = officeAddressService.updateOfficeAddress(doctorId, updateOfficeId,updateOfficeAddress);

        return addressUpdated
                ? ResponseEntity.status(HttpStatus.OK).body("Office successfully updated")
                : ResponseEntity.badRequest().body("Failed to update office address.");

    }

    //delete address by id -- make sure to later
    @DeleteMapping("/{officeId}")
    public ResponseEntity<String> deleteOfficeAddress(
            @PathVariable Long doctorId,
            @PathVariable Long officeId) {
        boolean addressDelete = officeAddressService.deleteDoctorOffice(doctorId , officeId);

        return addressDelete
                ? ResponseEntity.ok("Office address successfully deleted")
                : ResponseEntity.badRequest().body("Failed to delete office address.");
    }
}
