package com.wildy.notification.clients;

import com.wildy.notification.clients.DTO.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientFeignClient {
    @GetMapping("/api/v1/patients/{id}")
    PatientDTO getPatientById(@PathVariable Long id);
}
