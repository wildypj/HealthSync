package com.wildy.analytics.clients;

import com.wildy.analytics.clients.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientFeignClient {
    @GetMapping("/api/v1/patients/{id}")
    PatientDTO getPatientById(@PathVariable Long id);
}
