package com.carloscaro.medicaidservice.claim;

import com.carloscaro.medicaidservice.common.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ClaimServiceIntegrationTest {
    @Autowired
    ClaimService service;
    @PersistenceContext
    EntityManager em;

    private static final String CLAIM_ID = "TEST_ID";
    private static final String PRESCRIPTION_ID = "P4"; //must exist in test DB
    private static final String NOT_FOUND_PRESCRIPTION_ID = "Z4";
    private static final LocalDate SERVICE_DATE = LocalDate.of(2026, 1, 12);

    @Test
    void createThenFindReturnsStoredClaim() {
        Optional<Claim> emptyClaim = service.find(CLAIM_ID);
        Claim createdClaim = service.create(PRESCRIPTION_ID, SERVICE_DATE);
        String claimId = createdClaim.getId();
        em.flush();
        em.clear();
        Optional<Claim> actualClaim = service.find(claimId);
        Assertions.assertTrue(emptyClaim.isEmpty());
        Assertions.assertTrue(actualClaim.isPresent());
        Assertions.assertEquals(claimId, actualClaim.get().getId());
        Assertions.assertEquals(PRESCRIPTION_ID, actualClaim.get().getPrescriptionId());
        Assertions.assertEquals("PENDING", actualClaim.get().getStatus());
        Assertions.assertEquals(SERVICE_DATE, actualClaim.get().getServiceDate());
    }

    @Test
    void missingPrescriptionThrowsNotFoundException() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> service.create(NOT_FOUND_PRESCRIPTION_ID, SERVICE_DATE)
        );
        Assertions.assertEquals("Prescription not found", e.getMessage());
    }

    @Test
    void prescriptionWithExistingClaimThrowsConstraintViolationException() {
        service.create(PRESCRIPTION_ID, SERVICE_DATE);
        em.flush();
        em.clear();
        ConstraintViolationException e = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> {
                    service.create(PRESCRIPTION_ID, SERVICE_DATE);
                    em.flush();
                    em.clear();
                }
        );
        Assertions.assertEquals("23505", e.getSQLState());
        Assertions.assertEquals("unique_prescription_id", e.getConstraintName());
    }
}
