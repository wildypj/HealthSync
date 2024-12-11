package com.wildy.doctor.officeAddress;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OfficeAddressController.class)
class OfficeAddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OfficeAddressService officeAddressService;

    @Test
    void createOfficeAddress_returnStatusOk() throws Exception{
        Long doctorId = 1L;
        OfficeAddress officeAddress = new OfficeAddress();
        officeAddress.setStreet("456 Elm Street");
        officeAddress.setCity("Springfield");
        officeAddress.setState("IL");
        officeAddress.setCountry("USA");
        officeAddress.setZipCode("62704");

        Mockito.when(officeAddressService.addDoctorOffice(Mockito.eq(doctorId), Mockito.any(OfficeAddress.class))).thenReturn(true);

        mockMvc.perform(post("/api/{doctorId}/office-addresses", doctorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(officeAddress)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Office successfully created"));
    }

    @Test
    void createOfficeAddress_returnStatusNotFound() throws Exception{
        Long doctorId = 1L;
        OfficeAddress officeAddress = new OfficeAddress();
        officeAddress.setStreet("456 Elm Street");

        Mockito.when(officeAddressService.addDoctorOffice(Mockito.eq(doctorId), Mockito.any(OfficeAddress.class))).thenReturn(false);

        mockMvc.perform(post("/api/{doctorId}/office-addresses", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(officeAddress)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to create address. Can only Have one Office Address"));

    }

    @Test
    void updateOfficeAddress() throws Exception{
        Long doctorId = 1L;
        Long officeAddressId = 2L;

        OfficeAddress updatedAddress = new OfficeAddress();
        updatedAddress.setStreet("New Street");
        updatedAddress.setCity("Updated City");
        updatedAddress.setState("Updated State");
        updatedAddress.setCountry("Updated Country");
        updatedAddress.setZipCode("54321");

        Mockito.when(officeAddressService.updateOfficeAddress(Mockito.eq(doctorId), Mockito.eq(officeAddressId), Mockito.any(OfficeAddress.class))).thenReturn(true);

        mockMvc.perform(put("/api/{doctorId}/office-addresses/{updateOfficeId}", doctorId, officeAddressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(content().string("Office successfully updated"));

    }

    @Test
    void updateOfficeAddress_returnStatusBadRequest() throws Exception {
        Long doctorId = 1L;
        Long officeAddressId = 2L;

        OfficeAddress updatedAddress = new OfficeAddress();
        updatedAddress.setStreet("New Street");

        Mockito.when(officeAddressService.updateOfficeAddress(Mockito.eq(doctorId), Mockito.eq(officeAddressId), Mockito.any(OfficeAddress.class))).thenReturn(false);

        mockMvc.perform(put("/api/{doctorId}/office-addresses/{updateOfficeId}", doctorId, officeAddressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to update office address."));
    }

    @Test
    void deleteOfficeAddress_returnStatusOk() throws Exception{
        Long doctorId = 1L;
        Long officeAddressId = 2L;

        Mockito.when(officeAddressService.deleteDoctorOffice(Mockito.eq(doctorId), Mockito.eq(officeAddressId))).thenReturn(true);

        mockMvc.perform(delete("/api/{doctorId}/office-addresses/{updateOfficeId}", doctorId, officeAddressId))
                .andExpect(status().isOk())
                .andExpect(content().string("Office address successfully deleted"));
    }

    @Test
    void deleteOfficeAddress_returnStatusBadRequest() throws Exception{
        Long doctorId = 1L;
        Long officeAddressId = 2L;

        Mockito.when(officeAddressService.deleteDoctorOffice(Mockito.eq(doctorId), Mockito.eq(officeAddressId))).thenReturn(false);

        mockMvc.perform(delete("/api/{doctorId}/office-addresses/{updateOfficeId}", doctorId, officeAddressId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to delete office address."));
    }
}