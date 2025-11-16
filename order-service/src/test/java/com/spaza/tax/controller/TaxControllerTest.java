package com.spaza.tax.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.spaza.order.OrderServiceApplication.class)
@AutoConfigureMockMvc
class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTaxClass_ShouldReturn200() throws Exception {
        String json = "{\"code\":\"VAT_STANDARD\",\"title\":\"Standard VAT\",\"merchantId\":1}";
        
        mockMvc.perform(post("/api/v1/private/tax/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void createTaxRate_ShouldReturn200() throws Exception {
        String json = "{\"code\":\"ZA_VAT_15\",\"taxClassId\":1,\"countryCode\":\"ZA\",\"rate\":15.0,\"merchantId\":1}";
        
        mockMvc.perform(post("/api/v1/private/tax/rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void listTaxRates_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/private/tax/rates?merchantId=1"))
                .andExpect(status().isOk());
    }
}
