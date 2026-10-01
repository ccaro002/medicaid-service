package com.carloscaro.medicaidservice.drug;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DrugServiceIntegrationTest {
    @Autowired
    DrugService service;
    @PersistenceContext
    EntityManager em;

    private static final String DRUG_ID = "T1";
    private static final String DRUG_NAME = "TestDrug";
    private static final String OTHER_DRUG_NAME = "OtherDrug";

    @Test
    void createThenFindReturnsStoredDrug() {
        Optional<Drug> emptyDrug = service.find(DRUG_ID);
        service.create(DRUG_ID, DRUG_NAME);
        em.flush();
        em.clear();
        Drug actualDrug = service.find(DRUG_ID).orElseThrow();
        assertTrue(emptyDrug.isEmpty());
        assertEquals(DRUG_ID, actualDrug.getId());
        assertEquals(DRUG_NAME, actualDrug.getName());
    }

    @Test
    void duplicateDrugIdIsRejected() {
        service.create(DRUG_ID, DRUG_NAME);
        em.flush();
        em.clear();
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            service.create(DRUG_ID, OTHER_DRUG_NAME);
            em.flush();
        });
        assertEquals("23505", exception.getSQLState());
    }
}
