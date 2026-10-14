package com.carloscaro.medicaidservice.claim;

import com.carloscaro.medicaidservice.common.NotFoundException;
import com.carloscaro.medicaidservice.prescription.PrescriptionService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClaimService {
    private final PrescriptionService prescriptionService;
    private final ClaimRepository repo;
    private final EntityManager em;

    @Transactional
    public Claim create(final String prescriptionId, final LocalDate serviceDate) {
            prescriptionService.find(prescriptionId).orElseThrow(() -> new NotFoundException("Prescription not found"));
            String claimId = UUID.randomUUID().toString();
            Claim claim = new Claim(claimId, prescriptionId, "PENDING", serviceDate);
            em.persist(claim);
            em.flush();
            return claim;
    }

    public Optional<Claim> find(final String id) { return repo.findById(id); }
}
