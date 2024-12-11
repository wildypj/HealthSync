package com.wildy.patient.address;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{patientId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<String> createAddress(
            @PathVariable Long patientId,
            @RequestBody Address address) {
        boolean addressAdded = addressService.addPatientAddress(patientId ,address);
        return addressAdded
                ? ResponseEntity.status(HttpStatus.CREATED).body("Address successfully deleted.")
                : ResponseEntity.badRequest()
                .body("Failed to create address. Patient already has 2 addresses or patient not found.");

    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deletePatientAddress(
            @PathVariable Long patientId,
            @PathVariable("addressId") Long addressId) {
        boolean addressDeleted = addressService.deletePatientAddress(patientId ,addressId);
        return addressDeleted
                ? ResponseEntity.ok("Address successfully deleted.")
                : ResponseEntity.badRequest().body("Failed to delete address. Address not found.");
    }
}
