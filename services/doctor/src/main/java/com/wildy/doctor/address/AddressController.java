package com.wildy.doctor.address;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{doctorId}/home-addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // create home address by doctor ID
    @PostMapping
    public ResponseEntity<String> createAddress(
            @PathVariable Long doctorId,
            @RequestBody Address address) {
        boolean addressAdded = addressService.addDoctorAddress(doctorId, address);
        return addressAdded
                ? ResponseEntity.status(HttpStatus.CREATED).body("Address successfully created.")
                : ResponseEntity.badRequest()
                .body("Failed to create address. Doctor not found or already has 4 addresses.");

    }

    // Delete address by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long doctorId,
            @PathVariable Long id) {
        boolean addressDeleted = addressService.deleteDoctorAddress(doctorId, id);
        return addressDeleted
                ? ResponseEntity.ok("Address successfully deleted.")
                : ResponseEntity.badRequest().body("Failed to delete address.");
    }
}
