package com.wildy.doctor.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAddress_returnStatusCreated() throws Exception {
        //Arrange
        Long doctorId = 1L;
        Address address = new Address();
        address.setStreet("123 Main Street");
        address.setCity("Springfield");
        address.setState("IL");
        address.setCountry("USA");
        address.setZipCode("62704");

        Mockito.when(addressService.addDoctorAddress(Mockito.eq(doctorId), Mockito.any(Address.class))).thenReturn(true);

        mockMvc.perform(post("/api/{doctorId}/home-addresses", doctorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Address successfully created."));
    }

    @Test
    void createAddress_returnStatusBadRequest() throws Exception {
        Long doctorId = 1L;
        Address address = new Address();
        address.setStreet("123 Main Street");
        address.setCity("Springfield");
        address.setState("IL");
        address.setCountry("USA");
        address.setZipCode("62704");

        Mockito.when(addressService.addDoctorAddress(Mockito.eq(doctorId), Mockito.any(Address.class))).thenReturn(false);

        mockMvc.perform(post("/api/{doctorId}/home-addresses", doctorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to create address. Doctor not found or already has 4 addresses."));
    }

    @Test
    void deleteAddress_returnStatusOk() throws Exception {
        Long doctorId = 1L;
        Long addressId = 2L;

        Mockito.when(addressService.deleteDoctorAddress(Mockito.eq(doctorId), Mockito.eq(addressId))).thenReturn(true);

        mockMvc.perform(delete("/api/{doctorId}/home-addresses/{id}", doctorId, addressId))
                .andExpect(status().isOk())
                .andExpect(content().string("Address successfully deleted."));
    }

    @Test
    void deleteAddress_returnStatusBadRequest() throws Exception {
        Long doctorId = 1L;
        Long addressId = 2L;

        Mockito.when(addressService.deleteDoctorAddress(Mockito.eq(doctorId), Mockito.eq(addressId))).thenReturn(false);

        mockMvc.perform(delete("/api/{doctorId}/home-addresses/{id}", doctorId, addressId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to delete address."));
    }
}