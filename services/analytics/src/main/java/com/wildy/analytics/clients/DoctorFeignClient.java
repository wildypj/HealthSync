package com.wildy.analytics.clients;

import com.wildy.analytics.clients.dto.DoctorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorFeignClient {
    @GetMapping("/api/v1/doctors/{id}")
    DoctorDTO getDoctorById(@PathVariable Long id);
}
