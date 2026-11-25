package com.logistics.shipment_service.controller;

import com.logistics.shipment_service.domain.Shipment;
import com.logistics.shipment_service.domain.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShipmentRepository shipmentRepository;

    @Test
    void getAllShipments_returnsShipmentsFromRepository() throws Exception {
        Shipment shipment = new Shipment(1L, "TRK123", "CREATED");
        when(shipmentRepository.findAll()).thenReturn(List.of(shipment));

        mockMvc.perform(get("/api/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].trackingNumber").value("TRK123"))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }
}