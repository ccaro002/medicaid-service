package com.carloscaro.medicaidservice.drug;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DrugController.class)
class DrugControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DrugService drugService;

    private static final String URI = "/drugs/{id}";
    private static final String DRUG_ID = "D1";
    private static final String DRUG_NAME = "Alpha";
    private static final String MISSING_DRUG = "missing";

    @Test
    void existingDrugReturns200AndDto() throws Exception {
        when(drugService.find(DRUG_ID))
                .thenReturn(Optional.of(new Drug(DRUG_ID, DRUG_NAME)));

        mockMvc.perform(get(URI, DRUG_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DRUG_ID))
                .andExpect(jsonPath("$.name").value(DRUG_NAME));
    }

    @Test
    void missingDrugReturns404() throws Exception {
        when(drugService.find(MISSING_DRUG))
                .thenReturn(Optional.empty());
        mockMvc.perform(get(URI, MISSING_DRUG)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Drug not found"));
    }
}