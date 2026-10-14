package com.carloscaro.medicaidservice.claim;

import com.carloscaro.medicaidservice.common.NotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClaimController.class)
public class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClaimService claimService;

    private static final String GET_URI = "/claims/{id}";
    private static final String POST_URI = "/claims";
    private static final String CLAIM_ID = "TEST_CLAIM_ID";
    private static final String MISSING_CLAIM = "missing";
    private static final String PRESCRIPTION_ID = "TEST_PRESCRIPTION_ID";
    private static final String STATUS = "PENDING";
    private static final LocalDate SERVICE_DATE = LocalDate.of(2026, 1, 12);
    private static final Claim CLAIM = new Claim(CLAIM_ID, PRESCRIPTION_ID, STATUS, SERVICE_DATE);
    private static final String REQUEST_BODY = """
            {
                "prescriptionId": "%s",
                "serviceDate": "%s"
            }
            """.formatted(PRESCRIPTION_ID, SERVICE_DATE);

    @Test
    void existingClaimReturns200AndDto() throws Exception {
        when(claimService.find(CLAIM_ID)).thenReturn(Optional.of(CLAIM));
        mockMvc.perform(get(GET_URI, CLAIM_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CLAIM_ID))
                .andExpect(jsonPath("$.prescriptionId").value(PRESCRIPTION_ID))
                .andExpect(jsonPath("$.status").value(STATUS))
                .andExpect(jsonPath("$.serviceDate").value(SERVICE_DATE.toString()));
    }

    @Test
    void missingClaimReturns404() throws Exception {
        when(claimService.find(MISSING_CLAIM))
                .thenReturn(Optional.empty());
        mockMvc.perform(get(GET_URI, MISSING_CLAIM).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Claim not found"));
    }

    @Test
    void unexpectedErrorReturns500() throws Exception {
        when(claimService.find(CLAIM_ID)).thenThrow(new IllegalArgumentException());
        mockMvc.perform(get(GET_URI, CLAIM_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Unexpected error"));
    }

    @Test
    void createClaimReturns201AndDto() throws Exception {
        when(claimService.create(PRESCRIPTION_ID, SERVICE_DATE)).thenReturn(CLAIM);
        mockMvc.perform(post(POST_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(REQUEST_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/claims/" + CLAIM_ID))
                .andExpect(jsonPath("$.id").value(CLAIM_ID))
                .andExpect(jsonPath("$.prescriptionId").value(PRESCRIPTION_ID))
                .andExpect(jsonPath("$.status").value(STATUS))
                .andExpect(jsonPath("$.serviceDate").value(SERVICE_DATE.toString()));
    }

    @Test
    void createClaimWithMissingPrescriptionReturns404() throws Exception {
        when(claimService.create(PRESCRIPTION_ID, SERVICE_DATE)).thenThrow(new NotFoundException("Prescription not found"));
        mockMvc.perform(post(POST_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(REQUEST_BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Prescription not found"));
    }

    @Test
    void createClaimForPrescriptionWithExistingClaimReturns409() throws Exception {
        when(claimService.create(PRESCRIPTION_ID, SERVICE_DATE)).thenThrow(new ConstraintViolationException(
                "Test error",
                new SQLException(),
                "unique_prescription_id"
        ));
        mockMvc.perform(post(POST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(REQUEST_BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Prescription has a claim"));
    }

    @Test
    void createClaimWithBlankPrescriptionIdReturns400() throws Exception {
        String requestBody = """
            {
                "prescriptionId": "",
                "serviceDate": "%s"
            }
            """.formatted(SERVICE_DATE);

        mockMvc.perform(post(POST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid fields"));

        verifyNoInteractions(claimService);
    }

    @Test
    void createClaimWithMalformedServiceDateReturns400() throws Exception {
        String requestBody = """
            {
                "prescriptionId": "%s",
                "serviceDate": "20260901"
            }
            """.formatted(PRESCRIPTION_ID);

        mockMvc.perform(post(POST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid fields"));

        verifyNoInteractions(claimService);
    }
}