package za.blkmarket.userauth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MerchantStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createStore_ShouldReturn200() throws Exception {
        String code = "TEST_" + System.currentTimeMillis();
        String json = "{\"code\":\"" + code + "\",\"name\":\"Test Store\",\"email\":\"test@store.com\",\"phone\":\"+27123456789\"}";
        
        mockMvc.perform(post("/api/v1/private/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code));
    }

    @Test
    void getStore_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/private/stores/SPAZA_HQ"))
                .andExpect(status().isOk());
    }

    @Test
    void listStores_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/private/stores?page=0&size=10"))
                .andExpect(status().isOk());
    }
}
